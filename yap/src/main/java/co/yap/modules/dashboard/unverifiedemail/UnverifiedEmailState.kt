package co.yap.modules.dashboard.unverifiedemail

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.yapcore.BaseState

class UnverifiedEmailState : BaseState(), IUnverifiedEmail.State {

    @get:Bindable
    override var tootlBarTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.tootlBarTitle)

        }

    @get:Bindable
    override var tootlBarVisibility: Int = 0x00000000
        set(value) {
            field = value
            notifyPropertyChanged(BR.tootlBarVisibility)

        }
}