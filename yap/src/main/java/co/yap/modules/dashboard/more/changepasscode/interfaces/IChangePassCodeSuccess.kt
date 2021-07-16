package co.yap.modules.dashboard.more.changepasscode.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IChangePassCodeSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnDoneButton()
        val buttonClickEvent: SingleClickEvent
    }

    interface State : IBase.State {
        var title: String?
        var topSubHeading: String
    }
}