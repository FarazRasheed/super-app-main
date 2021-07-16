package co.yap.modules.otp

import android.app.Application
import android.content.Context
import co.yap.networking.authentication.AuthApi
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.messages.requestdtos.CreateForgotPasscodeOtpRequest
import co.yap.networking.messages.requestdtos.CreateOtpGenericRequest
import co.yap.networking.messages.requestdtos.VerifyForgotPasscodeOtpRequest
import co.yap.networking.messages.requestdtos.VerifyOtpGenericRequest
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.R
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.getColors
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.extentions.toast
import co.yap.yapcore.managers.SessionManager

class GenericOtpViewModel(application: Application) :
    BaseViewModel<IGenericOtp.State>(application = application), IGenericOtp.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val errorEvent: SingleClickEvent = SingleClickEvent()
    private val repository: MessagesRepository = MessagesRepository
    override var token: String? = ""
    override val state: GenericOtpState = GenericOtpState(application = application)
    private val authRepository: AuthApi = AuthRepository


    override fun onCreate() {
        super.onCreate()
        when (state.otpDataModel?.otpAction) {
            OTPActions.CHANGE_EMAIL.name -> {
                setVerificationTitle(Strings.screen_email_verification_display_text_heading)
                setVerificationDescription()
            }
            OTPActions.FORGOT_CARD_PIN.name -> {
                setVerificationTitle(Strings.screen_forgot_pin_display_text_heading)
                setVerificationDescription()
            }
            OTPActions.FORGOT_PASS_CODE.name -> {
                setVerificationTitle(Strings.screen_forgot_passcode_otp_display_text_heading)
                setVerificationDescription()
            }
            OTPActions.CHANGE_MOBILE_NO.name -> {
                setVerificationTitle(Strings.screen_forgot_passcode_otp_display_text_heading)
                state.verificationDescription =
                    getString(Strings.screen_change_phone_number_display_text_text_description).format(
                        state.mobileNumber[0]
                    )
            }
            OTPActions.DOMESTIC_TRANSFER.name, OTPActions.UAEFTS.name, OTPActions.SWIFT.name, OTPActions.RMT.name, OTPActions.CASHPAYOUT.name, OTPActions.Y2Y.name -> {
                state.verificationTitle =
                    state.otpDataModel?.username ?: ""
                val descriptionString =
                    getString(Strings.screen_cash_pickup_funds_display_otp_text_description).format(
                        state.currencyType,
                        state.otpDataModel?.amount?.toFormattedCurrency(
                            showCurrency = false,
                            currency = state.currencyType ?: SessionManager.getDefaultCurrency()
                        ),
                        state.otpDataModel?.username
                    )
                state.verificationDescriptionForLogo =
                    Utils.getSppnableStringForAmount(
                        context,
                        descriptionString,
                        state.currencyType ?: "",
                        Utils.getFormattedCurrencyWithoutComma(state.otpDataModel?.amount)
                    )
            }
            else -> {
                setVerificationTitle(Strings.screen_forgot_passcode_otp_display_text_heading)
                setVerificationDescription()
            }
        }
    }

    override fun handlePressOnButtonClick(id: Int) {
        clickEvent.setValue(id)
    }

    override fun isValidOtpLength(otp: String): Boolean {
        state.valid = otp.isNotEmpty() && otp.length == 6
        return otp.isNotEmpty() && otp.length == 6
    }

    override fun handlePressOnResendClick(context: Context) {
        when (state.otpDataModel?.otpAction) {
            OTPActions.CHANGE_MOBILE_NO.name -> createOtpForPhoneNumber(true, context)
            OTPActions.FORGOT_PASS_CODE.name -> createForgotPassCodeOtpRequest(true, context)
            else -> createOtp(true, context)
        }
    }

    override fun verifyOtp(success: () -> Unit) {
        when (state.otpDataModel?.otpAction) {
            OTPActions.CHANGE_MOBILE_NO.name -> {
                launch {
                    state.loading = true
                    when (val response =
                        repository.verifyOtpGenericWithPhone(
                            state.mobileNumber[0]?.replace(" ", "")?.replace("+", "00") ?: "",
                            VerifyOtpGenericRequest(
                                state.otpDataModel?.otpAction ?: "",
                                state.otp.get() ?: ""
                            )
                        )
                        ) {
                        is RetroApiResponse.Success -> {
                            success.invoke()
                        }
                        is RetroApiResponse.Error -> {
                            showToast(response.error.message)
                            state.otp.set("")
                            otpUiBlocked(response.error.actualCode)
                            state.loading = false
                        }
                    }
                    state.loading = false
                }
            }
            OTPActions.FORGOT_PASS_CODE.name -> {
                verifyForgotPassCodeOtp {
                    success.invoke()
                }
            }
            else -> {
                launch {
                    state.loading = true
                    when (val response =
                        repository.verifyOtpGeneric(
                            VerifyOtpGenericRequest(
                                state.otpDataModel?.otpAction ?: "",
                                state.otp.get() ?: ""
                            )
                        )) {
                        is RetroApiResponse.Success -> {
                            response.data.token?.let {
                                val tokens = it.split("%")
                                token = tokens.first()
                                if (tokens.size > 1)
                                    authRepository.setJwtToken(tokens.last())
                            }
                            success.invoke()
                        }
                        is RetroApiResponse.Error -> {
                            state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                            state.otp.set("")
                            state.loading = false
                            otpUiBlocked(response.error.actualCode)
                        }
                    }
                    state.loading = false
                }
            }
        }
    }

    private fun verifyForgotPassCodeOtp(success: () -> Unit) {
        launch {
            state.loading = true
            when (val response =
                repository.verifyForgotPasscodeOtp(
                    VerifyForgotPasscodeOtpRequest(
                        state.otpDataModel?.username.toString(),
                        state.otp.get() ?: "",
                        state.otpDataModel?.emailOtp ?: false
                    )
                )) {
                is RetroApiResponse.Success -> {
                    response.data.token?.let {
                        val tokens = it.split("%")
                        token = tokens.first()
                        if (tokens.size > 1)
                            authRepository.setJwtToken(tokens.last())
                    }
                    success.invoke()
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.otp.set("")
                    otpUiBlocked(response.error.actualCode)
                }
            }
            state.loading = false
        }
    }

    private fun getUserName(): String? {
        val sharedPreferenceManager = SharedPreferenceManager(context)
        return if (!SharedPreferenceManager(context).getValueBoolien(
                Constants.KEY_IS_USER_LOGGED_IN,
                false
            )
        ) {
            state.otpDataModel?.username
        } else {
            sharedPreferenceManager.getDecryptedUserName()
        }
    }

    override fun createOtp(resend: Boolean, context: Context) {
        launch {
            state.loading = true
            when (val response =
                repository.createOtpGeneric(
                    createOtpGenericRequest = CreateOtpGenericRequest(
                        state.otpDataModel?.otpAction ?: ""
                    )
                )) {
                is RetroApiResponse.Success -> {
                    handleResendEvent(resend, context)
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                    otpUiBlocked(response.error.actualCode)
                }
            }
            state.loading = false
        }
    }

    private fun createForgotPassCodeOtpRequest(
        resend: Boolean,
        context: Context
    ) {
        val username = getUserName()
        username?.let {
            launch {
                state.loading = true
                when (val response = repository.createForgotPasscodeOTP(
                    CreateForgotPasscodeOtpRequest(
                        Utils.verifyUsername(username),
                        !Utils.isUsernameNumeric(username)
                    )
                )) {
                    is RetroApiResponse.Success -> {
                        response.data.data?.let {
                            state.otpDataModel?.mobileNumber = it

                            state.mobileNumber[0] = getFormattedPhoneNo(it)
                            setVerificationDescription()
                        }
                        handleResendEvent(resend, context)

                    }
                    is RetroApiResponse.Error -> {
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                        state.loading = false
                    }
                }
                state.loading = false
            }
        } ?: toast(context, "Invalid user name")
    }

    override fun initializeData(context: Context) {
        when (state.otpDataModel?.otpAction) {
            OTPActions.CHANGE_MOBILE_NO.name -> createOtpForPhoneNumber(false, context)
            OTPActions.FORGOT_PASS_CODE.name -> {
                state.reverseTimer(10, context)
                state.validResend = false
            }// createForgotPassCodeOtpRequest(false, context)
            else -> createOtp(false, context)
        }
        state.otpDataModel?.mobileNumber?.let {
            state.mobileNumber[0] = getFormattedPhoneNo(it)
        }
    }

    private fun createOtpForPhoneNumber(resend: Boolean, context: Context) {
        launch {
            state.loading = true
            when (val response =
                repository.createOtpGenericWithPhone(
                    phone = state.mobileNumber[0]?.replace(
                        " ",
                        ""
                    )?.replace("+", "00") ?: "",
                    createOtpGenericRequest = CreateOtpGenericRequest(OTPActions.CHANGE_MOBILE_NO.name)
                )) {
                is RetroApiResponse.Success -> {
                    handleResendEvent(resend, context)
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    showToast(response.error.message)
                    otpUiBlocked(response.error.actualCode)
                }
            }
            state.loading = false
        }
    }

    private fun otpUiBlocked(errorCode: String) {
        when (errorCode) {
            "1095" -> {// otp just blocked
                state.validResend = false
//                state.valid = false // to disable confirm button
                state.color = context.getColors(R.color.disabled)
                state.isOtpBlocked.set(true)
            }
//            "1095" -> { // otp already blocked
//                state.isOtpBlocked.set(true)
//            }
        }
    }

    private fun getFormattedPhoneNo(mobileNumber: String): String {
        return when {
            mobileNumber.startsWith("00") ->
                Utils.getFormattedPhone(
                    mobileNumber.replaceRange(
                        0,
                        2,
                        "+"
                    )
                )
            mobileNumber.startsWith("+") -> Utils.getFormattedPhone(mobileNumber)
            else -> Utils.formatePhoneWithPlus(mobileNumber)
        }
    }

    private fun handleResendEvent(resend: Boolean, context: Context) {
        if (resend)
            state.toast =
                getString(Strings.screen_verify_phone_number_display_text_resend_otp_success)

        state.reverseTimer(10, context)
        state.validResend = false
    }

    private fun setVerificationTitle(title: String) {
        state.verificationTitle = getString(title)
    }

    private fun setVerificationDescription() {
        state.verificationDescription =
            getString(Strings.screen_verify_phone_number_display_text_sub_title).format(
                state.mobileNumber[0]
            )
    }
}
