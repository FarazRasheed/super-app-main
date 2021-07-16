package co.yap.modules.onboarding.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent


interface IMeetingConfirmation {

    interface view : IBase.View<viewModel>

    interface viewModel : IBase.ViewModel<State>{
        val goToDashboardButtonPressEvent:SingleLiveEvent<Boolean>
        fun handlePressOnGoToDashboard()
    }

    interface State : IBase.State
}