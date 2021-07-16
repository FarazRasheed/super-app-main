package co.yap.modules.setcardpin.interfaces

import android.text.TextWatcher
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ISetCardPin {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun loadData()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val EVENT_SET_CARD_PIN_SUCCESS: Int
            get() = 1

        var pincode: String
        val clickEvent: SingleClickEvent
        val forgotPasscodeclickEvent: SingleClickEvent
        var errorEvent: SingleClickEvent
        fun handlePressOnNextButton(id: Int)
        fun handlePressOnForgotPasscodeButton(id: Int)
        fun setCardPin(cardSerialNumber: String)
        fun changeCardPinRequest(
            oldPin: String,
            newPin: String,
            confirmPin: String,
            cardSerialNumber: String,
            id: Int
        )

        fun forgotCardPinRequest(cardSerialNumber: String, newPin: String,token:String)
        //forgot passcode variables
        var mobileNumber: String
    }

    interface State : IBase.State {
        var dialerError: String
        var pincode: String
        var valid: Boolean
        fun getTextWatcher(): TextWatcher
        var sequence: Boolean
        var similar: Boolean
        var titleSetPin: String
        var buttonTitle: String
        var flowType: String
        var oldPin: String
        var newPin: String
        var confirmPin: String
        var cardSerialNumber: String
        var forgotTextVisibility: Boolean
    }
}