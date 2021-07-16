package co.yap.modules.dashboard.yapit.topup.landing

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.yapcore.BaseState

class TopUpLandingState : BaseState(), ITopUpLanding.State {

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

    @get:Bindable
    override var rightButtonVisibility: Int = 0x00000000
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightButtonVisibility)
        }

    @get:Bindable
    override var leftButtonVisibility: Int = 0x00000000
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftButtonVisibility)
        }
}