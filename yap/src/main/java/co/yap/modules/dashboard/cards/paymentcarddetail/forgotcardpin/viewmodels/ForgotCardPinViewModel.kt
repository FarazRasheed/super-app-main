package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.viewmodels

import android.app.Application
import android.view.View
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.interfaces.IForgotCardPin
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.states.ForgotCardPinState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.interfaces.OnItemClickListener

class ForgotCardPinViewModel(application: Application) :
    ForgotCardPinBaseViewModel<IForgotCardPin.State>(application), IForgotCardPin.ViewModel {
    override val backButtonPressEvent: SingleClickEvent = SingleClickEvent()
    override val state: ForgotCardPinState = ForgotCardPinState()

    override fun handlePressOnBackButton() {
        backButtonPressEvent.call()
    }


    /*   override fun onResume() {
         //  super.onResume()
           toggleToolBarVisibility(true)
       }*/
}