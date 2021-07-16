package co.yap.modules.dashboard.yapit.topup.topupbankdetails

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITopUpBankDetails {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent:SingleClickEvent
        fun handlePressOnButton(id: Int)
    }

    interface State : IBase.State {
        var toolBarVisibility: Boolean
        var pictureUrl: String?
        var position: Int?
    }
}