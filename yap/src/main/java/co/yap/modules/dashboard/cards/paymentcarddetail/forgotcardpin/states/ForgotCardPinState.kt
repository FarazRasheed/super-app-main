package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.interfaces.IForgotCardPin
import co.yap.yapcore.BaseState

class ForgotCardPinState : BaseState(), IForgotCardPin.State {

    @get:Bindable
    override var toolBarVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.toolBarVisibility)
        }
    @get:Bindable
    override var toolBarTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.tootlBarTitle)
        }

    @get:Bindable
    override var currentScreen: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.currentScreen)
        }
}