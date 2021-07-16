package co.yap.modules.dashboard.cards.reordercard.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.cards.reordercard.interfaces.IRenewCard
import co.yap.modules.dashboard.cards.reordercard.states.RenewCardState
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.CardType
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class RenewCardViewModel(application: Application) :
    ReorderCardBaseViewModel<IRenewCard.State>(application), IRenewCard.ViewModel,
    IRepositoryHolder<TransactionsRepository> {
    override var reorderCardSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val repository: TransactionsRepository = TransactionsRepository
    private val cardRepository: CardsRepository = CardsRepository
    override var fee: String = "0.0"
    override var address: Address = Address()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override val state: IRenewCard.State = RenewCardState()

    override fun onCreate() {
        super.onCreate()
        state.availableCardBalance.set(
            SessionManager.cardBalance.value?.availableBalance.toString()
                .toFormattedCurrency(showCurrency = true, currency = SessionManager.getDefaultCurrency())
        )
        requestReorderCardFee(parentViewModel?.card?.cardType)
        requestGetAddressForPhysicalCard()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle("Reorder this card")
        parentViewModel?.state?.toolbarVisibility?.set(true)
        updateCardTypeState()
    }

    private fun updateCardTypeState() {
        if (parentViewModel?.card?.cardType == CardType.DEBIT.type)
            state.cardType.set("Primary Card")
        else
            state.cardType.set(CardType.PHYSICAL.type + " Card")
    }


    override fun requestReorderCardFee(cardType: String?) {
        cardType?.let {
            when (it) {
                CardType.DEBIT.type -> {
                    requestReorderDebitCardFee()
                }
                else -> {
                    requestReorderSupplementaryCardFee()
                }
            }
        }
    }

    override fun requestReorderCard() {
        address.cardSerialNumber = parentViewModel?.card?.cardSerialNumber
        parentViewModel?.card?.cardType?.let {
            when (it) {
                CardType.DEBIT.type -> {
                    requestReorderDebitCard(address)
                }
                else -> {
                    requestReorderSupplementaryCard(address)
                }
            }
        }
    }

    override fun requestReorderDebitCardFee() {
        launch {
            when (val response = repository.getDebitCardFee()) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        if (response.data.data?.feeType == Constants.FEE_TYPE_FLAT) {
                            val feeAmount = response.data.data?.tierRateDTOList?.get(0)?.feeAmount
                            val VATAmount = response.data.data?.tierRateDTOList?.get(0)?.vatAmount
                            fee =
                                feeAmount?.plus(VATAmount ?: 0.0).toString()
                                    .toFormattedCurrency(
                                        showCurrency = false,
                                        currency = SessionManager.getDefaultCurrency()
                                    )
                                    ?: "0.0"
                        }
                    } else {
                        fee = "0.0".toFormattedCurrency(
                            showCurrency = false,
                            currency = SessionManager.getDefaultCurrency()
                        )
                            ?: "0.0"
                    }

                    state.cardFee.set("${SessionManager.getDefaultCurrency()} $fee")
                }

                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.TOAST.name}"
                }
            }
        }
    }

    override fun requestReorderSupplementaryCardFee() {
        launch {
            when (val response = repository.getCardFee(CardType.PHYSICAL.type)) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        if (response.data.data?.feeType == Constants.FEE_TYPE_FLAT) {
                            val feeAmount = response.data.data?.tierRateDTOList?.get(0)?.feeAmount
                            val VATAmount = response.data.data?.tierRateDTOList?.get(0)?.vatAmount
                            fee =
                                feeAmount?.plus(VATAmount ?: 0.0).toString()
                                    .toFormattedCurrency(showCurrency = false, currency = SessionManager.getDefaultCurrency())
                                    ?: "0.0"
                        }
                    } else {
                        fee = "0.0".toFormattedCurrency(showCurrency = false,currency = SessionManager.getDefaultCurrency()) ?: "0.0"
                    }

                    state.cardFee.set("${SessionManager.getDefaultCurrency()} $fee")
                }

                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.TOAST.name}"
                }
            }
        }
    }

    override fun requestReorderDebitCard(address: Address) {
        launch {
            state.loading = true
            when (val response = cardRepository.reorderDebitCard(address)) {
                is RetroApiResponse.Success -> {
                    reorderCardSuccess.value = true
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                }
            }

        }
    }

    override fun requestReorderSupplementaryCard(address: Address) {
        launch {
            state.loading = true
            when (val response = cardRepository.reorderSupplementryCard(address)) {
                is RetroApiResponse.Success -> {
                    reorderCardSuccess.value = true
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                }
            }
        }
    }

    override fun requestGetAddressForPhysicalCard() {
        launch {
            state.loading = true
            when (val response = cardRepository.getUserAddressRequest()) {
                is RetroApiResponse.Success -> {
                    address = response.data.data
                    state.cardAddressTitle.set(address.address1 ?: "")
                    state.valid.set(!address.address1.isNullOrEmpty())
                    if (!address.address2.isNullOrEmpty()) {
                        state.cardAddressSubTitle.set(address.address2 ?: "")
                    }
                }
                is RetroApiResponse.Error -> {
                    state.valid.set(false)
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
            state.loading = false
        }
    }

}