/*
package co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels

import android.app.Application
import co.yap.modules.setcardpin.interfaces.ISetCardPin
import co.yap.modules.setcardpin.pinflow.IPin
import co.yap.modules.setcardpin.pinflow.PinState
import co.yap.modules.setcardpin.states.SetCardPinState
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.StringUtils

*/
/*
* The usage of this fragment is useless unless for parent usage
* please do refactor
* *//*

open class ChangeCardPinViewModel(application: Application) :
    BaseViewModel<IPin.State>(application),
    IPin.ViewModel {
    override val forgotPasscodeclickEvent: SingleClickEvent = SingleClickEvent()
    override var mobileNumber: String = ""
    override var pincode: String = ""
    override val state: PinState = PinState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var errorEvent: SingleClickEvent = SingleClickEvent()


    override fun onCreate() {
        super.onCreate()
        state.titleSetPin = getString(Strings.screen_current_card_pin_display_text_heading)
        state.buttonTitle = getString(Strings.screen_current_card_pin_display_button_next)
    }

    override fun handlePressOnNextButton(id: Int) {
        if (validateAggressively()) {
            clickEvent.setValue(id)
        }
    }

    override fun handlePressOnForgotPasscodeButton(id: Int) {
        //forgotPasscodeclickEvent.postValue(id)
    }

    override fun setCardPin(cardSerialName: String) {}
    override fun changeCardPinRequest(
        oldPin: String,
        newPin: String,
        confirmPin: String,
        cardSerialNumber: String,
        id: Int
    ) {
    }

    override fun forgotCardPinRequest(cardSerialNumber: String, newPin: String) {

    }

    private fun validateAggressively(): Boolean {
        val isSame = StringUtils.hasAllSameChars(state.pincode)
        val isSequenced = StringUtils.isSequenced(state.pincode)
        if (isSequenced) state.dialerError =
            getString(Strings.screen_confirm_card_pin_display_text_error_sequence)
        if (isSame) state.dialerError =
            getString(Strings.screen_confirm_card_pin_display_text_error_same_digits)
        return !isSame && !isSequenced
    }

}*/
