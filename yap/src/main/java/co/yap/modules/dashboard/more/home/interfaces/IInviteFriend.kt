package co.yap.modules.dashboard.more.home.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IInviteFriend {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnButton(id: Int)
        fun setUpStrings()
    }

    interface State : IBase.State {
        var inviteTitle: String
        var inviteDescription: String
        var referralLinkTextHeading: String
        var buttonTitle: String
    }
}