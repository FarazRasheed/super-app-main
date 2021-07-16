package com.yap.yapandroid.modules.login.viewmodels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.yap.yapandroid.modules.login.interfaces.IVerifyPasscode
import com.yap.yapandroid.modules.login.states.VerifyPasscodeState
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.authentication.requestdtos.LoginRequest
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.DemographicDataRequest
import co.yap.networking.customers.requestdtos.VerifyPasscodeRequest
import co.yap.networking.customers.responsedtos.AccountInfo
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.messages.requestdtos.CreateForgotPasscodeOtpRequest
import co.yap.networking.models.ApiError
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.leanplum.trackEventWithAttributes
import co.yap.yapcore.managers.SessionManager
import com.yap.yapandroid.main.MainChildViewModel
import java.util.concurrent.TimeUnit

class VerifyPasscodeViewModel(application: Application) :
    MainChildViewModel<IVerifyPasscode.State>(application),
    IVerifyPasscode.ViewModel, IRepositoryHolder<AuthRepository> {

    override val repository: AuthRepository = AuthRepository
    override val state: VerifyPasscodeState = VerifyPasscodeState(application)
    override val onClickEvent: MutableLiveData<Int> = MutableLiveData()
    override val loginSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val validateDeviceResult: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val createOtpResult: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override var isFingerprintLogin: Boolean = false
    private val customersRepository: CustomersRepository = CustomersRepository
    override var mobileNumber: String = ""
    override var EVENT_LOGOUT_SUCCESS: Int = 101
    override val accountInfo: MutableLiveData<AccountInfo> = MutableLiveData()
    private val messagesRepository: MessagesRepository = MessagesRepository
    private val authRepository: AuthRepository = AuthRepository

    private fun handleAttemptsError(error: ApiError) {
        when (error.actualCode) {
            "302" -> {
                showAccountBlockedError(getString(Strings.screen_verify_passcode_text_account_locked))
            }
            "303" -> {
                showBlockForSomeTimeError(error.message)
            }
            "1260" -> {
                state.isAccountFreeze.set(true)
                showAccountBlockedError(error.message)
            }
            else -> {
                state.dialerError = error.message
                loginSuccess.postValue(false)
            }
        }
    }

    override fun showAccountBlockedError(errorMessage: String) {
        state.dialerError = errorMessage
        state.isScreenLocked.set(true)
        state.isAccountLocked.set(true)
        state.valid = false
    }

    private fun showBlockForSomeTimeError(message: String) {
        TimeUnit.SECONDS.toMinutes(message.toLongOrNull() ?: 0)
        val totalSeconds = message.toLongOrNull() ?: 0
        startCountDownTimer(totalSeconds)
        state.isScreenLocked.set(true)
        state.isAccountLocked.set(false)
        state.valid = false
    }

    private fun startCountDownTimer(totalSeconds: Long) {
        val timer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(totalSeconds), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                state.dialerError =
                    "Too many attempts. Please wait for ${timerString(
                        minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    )}"
            }

            override fun onFinish() {
                state.isScreenLocked.set(false)
                state.dialerError = ""
                state.valid = false
            }
        }
        timer.start()
    }

    private fun timerString(minutes: Long, seconds: Long): String {
        return String.format(
            "%02d:%02d",
            minutes,
            seconds
        )
    }

    override fun login() {
        launch {
            state.loading = true
            when (val response = repository.login(
                LoginRequest(
                    grant_type = "client_credentials",
                    client_id = state.username,
                    client_secret = state.passcode,
                    device_id = state.deviceId
                )
            )) {
                is RetroApiResponse.Success -> {
                    if (!response.data.accessToken.isNullOrBlank()) {
                        authRepository.setJwtToken(response.data.accessToken)
                        validateDeviceResult.postValue(true)
                    } else {
                        validateDeviceResult.postValue(false)
                    }
                    parentViewModel?.signingInData?.clientId = state.username
                    parentViewModel?.signingInData?.clientSecret = state.passcode
                    parentViewModel?.signingInData?.deviceID = state.deviceId
                    parentViewModel?.signingInData?.token = response.data.id_token
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    handleAttemptsError(response.error)
                }
            }
        }
    }

    override fun verifyPasscode() {
        launch {
            state.loading = true
            when (customersRepository.validateCurrentPasscode(VerifyPasscodeRequest(passcode = state.passcode))) {
                is RetroApiResponse.Success -> {
                    loginSuccess.postValue(true)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    loginSuccess.postValue(false)
                    state.loading = false
                }
            }
        }
    }

    override fun getAccountInfo() {
        launch {
            when (val response = customersRepository.getAccountInfo()) {
                is RetroApiResponse.Success -> {
                    if (!response.data.data.isNullOrEmpty()) {
                        SessionManager.user = response.data.data[0]
                        accountInfo.postValue(response.data.data[0])
                        SessionManager.setupDataSetForBlockedFeatures()
                        trackEventWithAttributes(SessionManager.user)
                        state.loading = false
                    }
                }
                is RetroApiResponse.Error -> {
                    state.dialerError = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun createOtp() {
        launch {
            when (val response =
                customersRepository.generateOTPForDeviceVerification(
                    DemographicDataRequest(
                        clientId = state.username,
                        clientSecret = state.passcode,
                        deviceId = state.deviceId
                    )
                )) {
                is RetroApiResponse.Success -> {
                    createOtpResult.postValue(true)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.dialerError = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun createForgotPassCodeOtp(success: () -> Unit) {
        val username = state.username
        launch {
            state.loading = true
            when (val response = messagesRepository.createForgotPasscodeOTP(
                CreateForgotPasscodeOtpRequest(
                    Utils.verifyUsername(username),
                    !Utils.isUsernameNumeric(username)
                )
            )) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let {
                        mobileNumber = it
                        success()
                    }
                }
                is RetroApiResponse.Error -> {
                    state.dialerError = response.error.message
                    state.loading = false
                }
            }
            state.loading = false
        }
    }

    fun logout(success: () -> Unit) {
        val deviceId: String? =
            SharedPreferenceManager(context).getValueString(co.yap.yapcore.constants.Constants.KEY_APP_UUID)
        launch {
            state.loading = true
            when (repository.logout(deviceId.toString())) {
                is RetroApiResponse.Success -> {
                    success.invoke()
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    success.invoke()
                    state.loading = false
                }
            }
        }
    }

    override fun handlePressOnPressView(id: Int) {
        onClickEvent.value = id
    }
}
