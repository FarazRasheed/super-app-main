package co.yap.modules.onboarding.viewmodels

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import co.yap.modules.onboarding.interfaces.IMobile
import co.yap.modules.onboarding.states.MobileState
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.messages.requestdtos.CreateOtpOnboardingRequest
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.adjust.AdjustEvents
import co.yap.yapcore.leanplum.trackEvent
import co.yap.yapcore.leanplum.SignupEvents
import co.yap.yapcore.AdjustEvents.Companion.trackAdjustPlatformEvent
import java.util.*

class MobileViewModel(application: Application) :
    OnboardingChildViewModel<IMobile.State>(application),
    IMobile.ViewModel, IRepositoryHolder<MessagesRepository> {

    override val repository: MessagesRepository = MessagesRepository
    override val state: MobileState = MobileState(application, this)
    override val nextButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    override fun onResume() {
        super.onResume()
        setProgress(20)
    }

    override fun onCreate() {
        super.onCreate()
        trackAdjustPlatformEvent(AdjustEvents.SIGN_UP_START.type)
    }

    override fun getCcp(editText: EditText) {
        editText.requestFocus()
        state.etMobileNumber = editText
        state.etMobileNumber?.requestFocus()
        state.etMobileNumber?.setOnEditorActionListener(onEditorActionListener())
    }

    override fun handlePressOnNext() {
        // Record the updatedDate
        parentViewModel?.onboardingData?.startTime = Date()
        // Send OTP request
        createOtp()
    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (state.valid) {
                    handlePressOnNext()
                }
            }
            false
        }
    }

    private fun createOtp() {
        var mobileNumber: String = state.mobile.trim().replace(state.countryCode.trim(), "")
        mobileNumber = state.mobile.trim().replace(" ", "")
        val formattedMobileNumber: String =
            state.countryCode.trim() + " " + state.mobile.trim().replace(
                state.countryCode.trim(),
                ""
            )
        val countryCode: String = state.countryCode.trim().replace("+", "00")
        trackEvent(SignupEvents.SIGN_UP_NUMBER.type, countryCode + mobileNumber)
        launch {
            state.loading = true
            when (val response = repository.createOtpOnboarding(
                CreateOtpOnboardingRequest(
                    countryCode,
                    mobileNumber,
                    parentViewModel?.onboardingData?.accountType.toString()
                )
            )) {
                is RetroApiResponse.Success -> {
                    nextButtonPressEvent.value = true
                    parentViewModel?.onboardingData?.countryCode = countryCode
                    parentViewModel?.onboardingData?.mobileNo = mobileNumber
                    parentViewModel?.onboardingData?.formattedMobileNumber = formattedMobileNumber
                }
                is RetroApiResponse.Error -> {
                    state.error = response.error.message
                    state.mobileError = response.error.message
                    trackEvent(SignupEvents.SIGN_UP_NUMBER_ERROR.type, response.error.message)
                }
            }
            state.loading = false
        }
    }
}