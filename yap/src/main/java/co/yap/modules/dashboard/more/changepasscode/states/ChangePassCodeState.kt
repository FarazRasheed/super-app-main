package co.yap.modules.dashboard.more.changepasscode.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import co.yap.modules.dashboard.more.changepasscode.interfaces.IChangePassCode
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseState

class ChangePassCodeState : BaseState(), IChangePassCode.State {

    @get:Bindable
    override var toolBarTitle: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.toolBarTitle)
        }

    override var toolbarVisibility: ObservableBoolean = ObservableBoolean()
    override var rightIcon: ObservableBoolean = ObservableBoolean()
    override var leftIcon: ObservableBoolean = ObservableBoolean()

}