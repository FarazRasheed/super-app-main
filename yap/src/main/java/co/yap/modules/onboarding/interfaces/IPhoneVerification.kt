package co.yap.modules.onboarding.interfaces

import android.content.Context
import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IPhoneVerification {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val nextButtonPressEvent: SingleClickEvent
        fun handlePressOnSendButton(id: Int)
        fun isValidOtpLength(otp: String): Boolean
        fun handlePressOnResendOTP(context: Context)
        fun verifyOtp(success: () -> Unit)
        fun setPasscode(passcode: String)
    }

    interface State : IBase.State {
        //views
        var verificationTitle: String
        var verificationDescription: String

        //properties
        var otp: ObservableField<String>
        var valid: Boolean
        var timer: String
        var validResend: Boolean
        fun reverseTimer(Seconds: Int, context: Context)
        var color: Int
        var isOtpBlocked: ObservableField<Boolean>
    }
}
