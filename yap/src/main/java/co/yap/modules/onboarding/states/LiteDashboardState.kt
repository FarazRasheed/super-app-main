package co.yap.modules.onboarding.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.onboarding.interfaces.ILiteDashboard
import co.yap.yapcore.BaseState

class LiteDashboardState : BaseState(), ILiteDashboard.State {

    @get:Bindable
    override var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    override var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

}