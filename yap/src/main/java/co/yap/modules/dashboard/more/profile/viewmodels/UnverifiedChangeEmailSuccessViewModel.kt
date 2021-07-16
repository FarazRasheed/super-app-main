package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.profile.intefaces.IUnverifiedChangeEmailSuccess
import co.yap.modules.dashboard.more.profile.states.UnverifiedChangeEmailSuccessState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class UnverifiedChangeEmailSuccessViewModel(application: Application) :
    BaseViewModel<IUnverifiedChangeEmailSuccess.State>(application),
    IUnverifiedChangeEmailSuccess.ViewModel {
    override val mailButtonClickEvent: SingleClickEvent = SingleClickEvent()
    override val state: UnverifiedChangeEmailSuccessState = UnverifiedChangeEmailSuccessState()

    override fun handlePressOnMailAppButton(id: Int) {
        mailButtonClickEvent.postValue(id)
    }

}