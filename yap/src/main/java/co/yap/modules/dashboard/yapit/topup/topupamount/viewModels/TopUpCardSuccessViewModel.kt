package co.yap.modules.dashboard.yapit.topup.topupamount.viewModels

import android.app.Application
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.ITopUpCardSuccess
import co.yap.modules.dashboard.yapit.topup.topupamount.states.TopUpCardSuccessState
import co.yap.networking.cards.responsedtos.CardBalance
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager


class TopUpCardSuccessViewModel(application: Application) :
    BaseViewModel<ITopUpCardSuccess.State>(application), ITopUpCardSuccess.ViewModel {

    override val state: TopUpCardSuccessState =
        TopUpCardSuccessState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        state.toolbarTitle = getString(Strings.screen_topup_success_display_text_title)
        state.buttonTitle =
            getString(Strings.screen_topup_success_display_text_dashboard_action_button_title)
        state.topUpSuccess =
            getString(Strings.screen_topup_success_display_text_success_transaction_message)

        state.topUpSuccess =
            getString(Strings.screen_topup_success_display_text_success_transaction_message).format(
                state.currencyType,
                state.amount.toFormattedCurrency(showCurrency = false,currency = SessionManager.getDefaultCurrency())
            )

    }

    fun getAccountBalanceRequest() {
        launch {
            state.loading = true
            when (val response = SessionManager.repository.getAccountBalanceRequest()) {
                is RetroApiResponse.Success -> {
                    SessionManager.cardBalance.value =
                        (CardBalance(availableBalance = response.data.data?.availableBalance.toString()))
                    kotlinx.coroutines.delay(1000)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                }
            }
        }
    }


    override fun buttonClickEvent(id: Int) {
        clickEvent.postValue(id)
    }
}