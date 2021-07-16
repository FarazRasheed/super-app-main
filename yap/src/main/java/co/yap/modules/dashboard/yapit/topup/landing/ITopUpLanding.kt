package co.yap.modules.dashboard.yapit.topup.landing

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITopUpLanding {

    interface State : IBase.State {
        var tootlBarTitle: String
        var tootlBarVisibility: Int
        var rightButtonVisibility: Int
        var leftButtonVisibility: Int
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnBackButton(id: Int)
        fun handlePressOnView(id: Int)
        val clickEvent: SingleClickEvent
    }

    interface View : IBase.View<ViewModel>
}