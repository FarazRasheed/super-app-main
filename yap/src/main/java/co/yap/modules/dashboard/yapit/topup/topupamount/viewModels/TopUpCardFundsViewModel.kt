package co.yap.modules.dashboard.yapit.topup.topupamount.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.viewmodels.FundActionsViewModel
import co.yap.networking.customers.models.Session
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.networking.customers.responsedtos.beneficiary.TopUpTransactionModel
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.Check3DEnrollmentSessionRequest
import co.yap.networking.transactions.requestdtos.CreateSessionRequest
import co.yap.networking.transactions.requestdtos.Order
import co.yap.translation.Strings
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.getValueWithoutComa
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager
import kotlinx.coroutines.delay

class TopUpCardFundsViewModel(application: Application) : FundActionsViewModel(application) {
    override val transactionsRepository: TransactionsRepository = TransactionsRepository
    override val htmlLiveData: MutableLiveData<String> = MutableLiveData()
    override val topUpTransactionModelLiveData: MutableLiveData<TopUpTransactionModel>? =
        MutableLiveData()
    private lateinit var topupCrad: TopUpCard
    private var secureId: String? = null
    private var orderId: String? = null

    override fun initateVM(item: TopUpCard) {
        topupCrad = item
        state.cardInfo.set(item)
        state.toolbarTitle = getString(Strings.screen_topup_transfer_display_text_screen_title)
        state.enterAmountHeading =
            getString(Strings.screen_topup_transfer_display_text_amount_title)
        state.currencyType = getString(Strings.common_text_currency_type)
        getFundTransferLimits(TransactionProductCode.TOP_UP_VIA_CARD.pCode)
        getFundTransferDenominations(TransactionProductCode.TOP_UP_VIA_CARD.pCode)
        state.availableBalanceGuide =
            getString(Strings.screen_topup_transfer_display_text_available_balance)
                .format(
                    state.currencyType,
                    SessionManager.cardBalance.value?.availableBalance.toString()
                        .toFormattedCurrency(showCurrency = false,currency = SessionManager.getDefaultCurrency())
                )
        state.buttonTitle = getString(Strings.screen_topup_funds_display_button_text)
    }

    override fun buttonClickEvent(id: Int) {
        if (state.checkValidityForAddTopUpFromExternalCard() == "") {
            clickEvent.postValue(id)
        } else {
            errorEvent.postValue(id)
        }
    }

    override fun createTransactionSession() {
        launch {
            state.loading = true
            when (val response = transactionsRepository.createTransactionSession(
                CreateSessionRequest(Order(state.currencyType, enteredAmount.value.getValueWithoutComa()))
            )) {
                is RetroApiResponse.Success -> {
                    orderId = response.data.data.order.id
                    response.data.data.session.id?.let {
                        check3DEnrollmentSessionRequest(
                            it,
                            response.data.data.order.id
                        )
                    }
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.errorDescription = response.error.message
                    errorEvent.call()
                }
            }
            //  state.loading = false
        }
    }

    private fun check3DEnrollmentSessionRequest(
        sessionId: String,
        orderId: String
    ) {
        launch {
            when (val response = transactionsRepository.check3DEnrollmentSession(
                Check3DEnrollmentSessionRequest(
                    topupCrad.id?.toIntOrNull(),
                    Order(state.currencyType, enteredAmount.value.getValueWithoutComa()),
                    Session(sessionId)
                )
            )) {
                is RetroApiResponse.Success -> {
                    secureId = response.data.data._3DSecureId
                    htmlLiveData.value =
                        response.data.data._3DSecure.authenticationRedirect.simple?.htmlBodyContent?.let { it }
                }
                is RetroApiResponse.Error -> {
                    state.errorDescription = response.error.message
                    errorEvent.call()
                }
            }
            state.loading = false
        }
    }

    override fun startPooling(showLoader: Boolean) {
        launch {
            if (showLoader)
                state.loading = true
            when (val response = transactionsRepository.secureIdPooling(secureId.toString())) {
                is RetroApiResponse.Success -> {
                    when (response.data.data) {
                        null -> {
                            delay(3000)
                            startPooling(false)
                        }
                        "N" -> {
                            state.toast = "unable to verify^${AlertType.DIALOG.name}"
                            state.loading = false
                        }
                        "Y" -> {
                            topUpTransactionModelLiveData?.value = TopUpTransactionModel(
                                orderId,
                                state.currencyType,
                                enteredAmount.value.getValueWithoutComa(),
                                topupCrad.id?.toInt(),
                                secureId
                            )
                            state.loading = false
                        }
                    }
                }
                is RetroApiResponse.Error -> {
                    state.errorDescription = response.error.message
                    errorEvent.call()
                }
            }
            state.loading = false
        }
    }

}