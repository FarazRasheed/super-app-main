package co.yap.modules.otp

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IGenericOtp {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun removeObservers()
        fun setResultData()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val errorEvent: SingleClickEvent
        fun handlePressOnButtonClick(id: Int)
        fun handlePressOnResendClick(context: Context)
        var token: String?
        fun createOtp(resend: Boolean = false, context: Context)
        fun initializeData(context: Context)
        fun isValidOtpLength(otp: String): Boolean
        fun verifyOtp(success: () -> Unit)
    }

    interface State : IBase.State {
        var otpDataModel: OtpDataModel?
        var verificationTitle: String
        var verificationDescription: String
        var mobileNumber: Array<String?>

        //properties
        var otp: ObservableField<String>
        var valid: Boolean
        var timer: String
        var validResend: Boolean
        fun reverseTimer(Seconds: Int, context: Context)
        var color: Int

        // Generic otp logo variables
        var verificationDescriptionForLogo: SpannableStringBuilder?
        var imageUrl: String?
        var currencyType: String?
        var amount: String?
        var beneficiaryCountry: String?
        var errorMessage: String?
        var isOtpBlocked: ObservableField<Boolean>
    }
}
