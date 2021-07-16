package co.yap.modules.dashboard.unverifiedemail

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent

class UnverifiedEmailViewModel(application: Application) :
    BaseViewModel<IUnverifiedEmail.State>(application),
    IUnverifiedEmail.ViewModel {
    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: UnverifiedEmailState =
        UnverifiedEmailState()

    override fun handlePressOnBackButton() {
        backButtonPressEvent.value = true
    }

}