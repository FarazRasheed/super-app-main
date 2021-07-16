package com.yap.yapandroid.modules.login.viewmodels

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import co.yap.R
import com.yap.yapandroid.modules.login.interfaces.IPhoneVerificationSignIn
import com.yap.yapandroid.modules.login.states.PhoneVerificationSignInState
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.DemographicDataRequest
import co.yap.networking.customers.responsedtos.AccountInfo
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.KEY_APP_UUID
import co.yap.yapcore.constants.Constants.KEY_IS_USER_LOGGED_IN
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.getColors
import co.yap.yapcore.leanplum.trackEventWithAttributes
import co.yap.yapcore.managers.SessionManager
import com.yap.yapandroid.main.MainChildViewModel

class PhoneVerificationSignInViewModel(application: Application) :
    MainChildViewModel<IPhoneVerificationSignIn.State>(application),
    IPhoneVerificationSignIn.ViewModel,
    IRepositoryHolder<AuthRepository> {

    override val repository: AuthRepository = AuthRepository
    override val state: PhoneVerificationSignInState =
        PhoneVerificationSignInState()
    override val postDemographicDataResult: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val customersRepository: CustomersRepository = CustomersRepository
    private val messagesRepository: MessagesRepository = MessagesRepository
    override val accountInfo: MutableLiveData<AccountInfo> = MutableLiveData()
    override var clickEvent: SingleClickEvent =SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        state.valid = false
    }

    override fun handlePressOnSendButton() {
        verifyOtp()
    }

    override fun verifyOtp() {
        launch {
            state.loading = true
            when (val response =
                customersRepository.verifyOTPForDeviceVerification(
                    DemographicDataRequest(
                        clientId = parentViewModel?.signingInData?.clientId,
                        clientSecret = parentViewModel?.signingInData?.clientSecret,
                        deviceId = parentViewModel?.signingInData?.deviceID,
                        otp = state.otp.get()
                    )
                )) {
                is RetroApiResponse.Success -> {
                    response.data.token?.let {
                        val tokens = it.split("%")
                        parentViewModel?.signingInData?.token = tokens.first()
                        if (tokens.size > 1)
                            repository.setJwtToken(tokens.last())
                    }
                    val sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
                    sharedPreferenceManager.save(
                        KEY_IS_USER_LOGGED_IN,
                        true
                    )
                    SessionManager.isRemembered.value?.let {
                        sharedPreferenceManager.save(Constants.KEY_IS_REMEMBER, it)
                    }

                    sharedPreferenceManager.savePassCodeWithEncryption(state.passcode)
                    sharedPreferenceManager.saveUserNameWithEncryption(state.username)
                    postDemographicData()
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.otp.set("")
                    otpUiBlocked(response.error.actualCode)
                }
            }
        }
    }

    fun isValidOtpLength(otp: String): Boolean {
        state.validateBtn = otp.isNotEmpty() && otp.length == 6
        return otp.isNotEmpty() && otp.length == 6
    }

    override fun handlePressOnResend(context: Context) {
        launch {
            state.loading = true
            when (val response =
                customersRepository.generateOTPForDeviceVerification(
                    DemographicDataRequest(
                        clientId = parentViewModel?.signingInData?.clientId,
                        clientSecret = parentViewModel?.signingInData?.clientSecret,
                        deviceId = parentViewModel?.signingInData?.deviceID
                    )
                )) {
                is RetroApiResponse.Success -> {
                    state.toast =
                        getString(Strings.screen_verify_phone_number_display_text_resend_otp_success)
                    state.reverseTimer(10, context)
                    state.valid = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    otpUiBlocked(response.error.actualCode)
                }
            }
            state.loading = false
        }
    }

    override fun postDemographicData() {
        val sharedPreferenceManager = SharedPreferenceManager(context)
        val deviceId: String? =
            sharedPreferenceManager.getValueString(KEY_APP_UUID)
        launch {
            state.loading = true
            when (val response =
                customersRepository.postDemographicData(
                    DemographicDataRequest(
                        "LOGIN",
                        Build.VERSION.RELEASE,
                        deviceId.toString(),
                        Build.BRAND,
                        if (Utils.isEmulator()) "generic" else Build.MODEL,
                        "Android",
                        parentViewModel?.signingInData?.token ?: ""
                    )
                )) {
                is RetroApiResponse.Success -> {
                    postDemographicDataResult.value = true
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }

    override fun getAccountInfo() {
        launch {
            when (val response = customersRepository.getAccountInfo()) {
                is RetroApiResponse.Success -> {
                    if (response.data.data.isNotEmpty()) {
                        SessionManager.user = response.data.data[0]
                        accountInfo.postValue(response.data.data[0])
                        SessionManager.setupDataSetForBlockedFeatures()
                        trackEventWithAttributes(
                            SessionManager.user
                        )
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                }
            }
        }
    }

    private fun otpUiBlocked(errorCode: String) {
        when (errorCode) {
            "1095" -> {
                state.valid = false
                state.color = context.getColors(R.color.disabled)
                state.isOtpBlocked.set(false)
            }
        }
    }
}
