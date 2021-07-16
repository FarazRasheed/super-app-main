package co.yap.modules.passcode

import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IPassCode {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var mobileNumber: String
        var token: String
        fun handlePressView(id: Int)
        fun validatePassCode(success: (isSuccess: Boolean) -> Unit)
        fun isValidPassCode(): Boolean
        fun updatePassCodeRequest(success: () -> Unit)
        fun forgotPassCodeRequest(success: () -> Unit)
        fun createForgotPassCodeOtp(success: (username:String) -> Unit)
        fun isUserLoggedIn(): Boolean
        fun setTitles(title: String, buttonTitle: String)
        fun setLayoutVisibility(visibility : Boolean?)
    }

    interface State : IBase.State {
        var passCode: String
        var dialerError: String
        var valid: Boolean
        fun getTextWatcher(): TextWatcher
        var title: String
        var buttonTitle: String
        var forgotTextVisibility: Boolean
        var needTermsConditions: Boolean?
        var toolbarVisibility: ObservableBoolean

    }
}