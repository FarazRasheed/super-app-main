package co.yap.modules.dashboard.more.changepasscode.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.changepasscode.interfaces.IChangePassCode
import co.yap.modules.dashboard.more.changepasscode.states.ChangePassCodeState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent


class ChangePassCodeViewModel(application: Application) :
    BaseViewModel<IChangePassCode.State>(application = application),
    IChangePassCode.ViewModel {
    override val state: ChangePassCodeState =
        ChangePassCodeState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        state.toolbarVisibility.set(true)
        state.leftIcon.set(true)
        state.rightIcon.set(true)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.postValue(id)
    }


}
