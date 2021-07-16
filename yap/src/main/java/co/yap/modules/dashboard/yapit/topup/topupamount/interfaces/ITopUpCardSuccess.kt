package co.yap.modules.dashboard.yapit.topup.topupamount.interfaces

import androidx.databinding.ObservableField
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITopUpCardSuccess {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun buttonClickEvent(id: Int)
        val clickEvent: SingleClickEvent
    }

    interface State : IBase.State {
        var cardInfo: ObservableField<TopUpCard>
        var formattedCardNo: ObservableField<String>
        var buttonTitle: String
        var topUpSuccess: String
        var currencyType: String
        var amount: String
        var availableBalanceSpanable: ObservableField<String>
    }
}