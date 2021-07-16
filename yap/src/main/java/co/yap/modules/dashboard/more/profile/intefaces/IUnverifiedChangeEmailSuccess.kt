package co.yap.modules.dashboard.more.profile.intefaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IUnverifiedChangeEmailSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnMailAppButton(id: Int)
        val mailButtonClickEvent:SingleClickEvent
    }

    interface State : IBase.State
}