package co.yap.modules.dashboard.yapit.y2y.main.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.yapit.y2y.main.interfaces.IY2Y
import co.yap.yapcore.BaseState

class Y2YState : BaseState(), IY2Y.State {

    @get:Bindable
    override var tootlBarVisibility: Int = 0x00000000
        set(value) {
            field = value
            notifyPropertyChanged(BR.tootlBarVisibility)

        }

    @get:Bindable
    override var rightButtonVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightButtonVisibility)
        }

    @get:Bindable
    override var leftButtonVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftButtonVisibility)
        }
}