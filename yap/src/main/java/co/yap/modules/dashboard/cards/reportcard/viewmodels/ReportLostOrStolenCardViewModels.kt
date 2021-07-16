package co.yap.modules.dashboard.cards.reportcard.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.reportcard.interfaces.IRepostOrStolenCard
import co.yap.modules.dashboard.cards.reportcard.states.ReportOrStolenCardState
import co.yap.modules.others.helper.Constants
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.requestdtos.CardsHotlistRequest
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.helpers.extentions.toFormattedCurrency

class ReportLostOrStolenCardViewModels(application: Application) :
    ReportLostOrStolenCardChildViewModels<IRepostOrStolenCard.State>(application),
    IRepostOrStolenCard.ViewModel,
    IRepositoryHolder<CardsRepository> {

    private val transactionRepository: TransactionsRepository = TransactionsRepository

    override var HOT_LIST_REASON: Int = 2
    val REASON_DAMAGE: Int = 2
    val REASON_LOST_STOLEN: Int = 4

    override val CARD_REORDER_SUCCESS: Int = 5000
    override var cardFee: String = "50"

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: ReportOrStolenCardState = ReportOrStolenCardState()
    override val repository: CardsRepository = CardsRepository
    private val messageRepository: MessagesRepository = MessagesRepository

    override fun onCreate() {
        super.onCreate()
        getHelpPhoneNo()
    }

    override fun onResume() {
        super.onResume()
        toggleReportCardToolBarVisibility(true)
        setToolBarTitle(getString(Strings.screen_report_card_display_text_title))
    }

    private fun getHelpPhoneNo() {
        launch {
            state.loading = true
            when (val response =
                messageRepository.getHelpDeskContact()) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let {
                        state.cautionMessage =
                            (getString(Strings.screen_report_card_display_text_block_footnote).format(
                                it
                            ))
                    }
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                }
            }
        }
    }

    override fun handlePressOnDamagedCard(id: Int) {
        state.valid = true
        HOT_LIST_REASON = REASON_DAMAGE
    }

    override fun handlePressOnLostOrStolen(id: Int) {
        state.valid = true
        HOT_LIST_REASON = REASON_LOST_STOLEN
    }

    override fun handlePressOnReportAndBlockButton(id: Int) {
        clickEvent.setValue(id)
    }


    override fun handlePressOnBackButton() {
        backButtonPressEvent.value = true
    }

    override fun requestConfirmBlockCard(card: Card) {

        val cardsHotlistReequest =
            CardsHotlistRequest(card.cardSerialNumber, HOT_LIST_REASON.toString())

        launch {
            state.loading = true
            when (val response = repository.reportAndBlockCard(cardsHotlistReequest)) {
                is RetroApiResponse.Success -> {

                    if (state.cardType == Translator.getString(
                            context,
                            Strings.screen_spare_card_landing_display_text_virtual_card
                        )
                    ) {
                        state.loading = false
                        toggleReportCardToolBarVisibility(false)
                        clickEvent.setValue(CARD_REORDER_SUCCESS)
                    } else {
                        if (parentViewModel?.card?.cardType == Constants.CARD_TYPE_DEBIT)
                            getDebitCardFee()
                        else
                            getPhysicalCardFee()
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                }
            }
        }
    }

    override fun getPhysicalCardFee() {
        launch {
            when (val response = transactionRepository.getCardFee("physical")) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        if (response.data.data?.feeType == co.yap.yapcore.constants.Constants.FEE_TYPE_FLAT) {
                            val feeAmount = response.data.data?.tierRateDTOList?.get(0)?.feeAmount
                            val VATAmount = response.data.data?.tierRateDTOList?.get(0)?.vatAmount
                            cardFee =
                                feeAmount?.plus(VATAmount ?: 0.0).toString()
                                    .toFormattedCurrency()
                        }
                    } else {
                        cardFee = "0.0".toFormattedCurrency()
                    }
                    toggleReportCardToolBarVisibility(false)
                    clickEvent.setValue(CARD_REORDER_SUCCESS)
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }

    override fun getDebitCardFee() {
        launch {
            when (val response = transactionRepository.getDebitCardFee()) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        if (response.data.data?.feeType == co.yap.yapcore.constants.Constants.FEE_TYPE_FLAT) {
                            val feeAmount = response.data.data?.tierRateDTOList?.get(0)?.feeAmount
                            val VATAmount = response.data.data?.tierRateDTOList?.get(0)?.vatAmount
                            cardFee =
                                feeAmount?.plus(VATAmount ?: 0.0).toString()
                                    .toFormattedCurrency()
                        }
                    } else {
                        cardFee = "0.0".toFormattedCurrency()
                    }
                    toggleReportCardToolBarVisibility(false)
                    clickEvent.setValue(CARD_REORDER_SUCCESS)
                }

                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }
}