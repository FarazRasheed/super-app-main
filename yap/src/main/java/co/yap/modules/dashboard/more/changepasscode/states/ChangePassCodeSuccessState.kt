package co.yap.modules.dashboard.more.changepasscode.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.more.changepasscode.interfaces.IChangePassCodeSuccess
import co.yap.yapcore.BaseState

class ChangePassCodeSuccessState : BaseState(), IChangePassCodeSuccess.State {
    @get:Bindable
    override var title: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }
    @get:Bindable
    override var topSubHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.topSubHeading)
        }
}