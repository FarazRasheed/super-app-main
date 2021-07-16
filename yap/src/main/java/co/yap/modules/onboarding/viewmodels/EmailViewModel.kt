package co.yap.modules.onboarding.viewmodels

import android.app.Application
import android.os.Build
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import co.yap.R
import co.yap.modules.onboarding.interfaces.IEmail
import co.yap.modules.onboarding.states.EmailState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.DemographicDataRequest
import co.yap.networking.customers.requestdtos.SaveReferalRequest
import co.yap.networking.customers.requestdtos.SendVerificationEmailRequest
import co.yap.networking.customers.requestdtos.SignUpRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.constants.Constants.KEY_APP_UUID
import co.yap.yapcore.constants.Constants.KEY_IS_USER_LOGGED_IN
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toast
import co.yap.yapcore.leanplum.SignupEvents
import co.yap.yapcore.leanplum.trackEvent
import co.yap.yapcore.managers.SessionManager
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit

class EmailViewModel(application: Application) :
    OnboardingChildViewModel<IEmail.State>(application), IEmail.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val state: EmailState = EmailState(application)
    override val nextButtonPressEvent: SingleClickEvent = SingleClickEvent()
    override val animationStartEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val repository: CustomersRepository = CustomersRepository
    private val sharedPreferenceManager = SharedPreferenceManager(context)

    override fun onResume() {
        super.onResume()
        setProgress(80)
    }

    override fun onCreate() {
        super.onCreate()
        state.emailTitle = getString(R.string.screen_enter_email_b2c_display_text_title)
        state.emailBtnTitle = getString(R.string.screen_phone_number_button_send)
        state.deactivateField = true
    }

    override fun handlePressOnNext() {
        if (state.emailTitle == getString(R.string.screen_email_verification_display_text_title)) {
            nextButtonPressEvent.setValue(EVENT_POST_DEMOGRAPHIC)
        } else {
            nextButtonPressEvent.setValue(EVENT_POST_VERIFICATION_EMAIL)
        }
    }

    override fun stopTimer() {
        parentViewModel?.onboardingData?.elapsedOnboardingTime =
            TimeUnit.MILLISECONDS.toSeconds(
                Date().time - (parentViewModel?.onboardingData?.startTime?.time ?: Date().time)
            )
    }


    private fun signUp() {
        launch {
            state.refreshField = true
            when (val response = repository.signUp(
                SignUpRequest(
                    parentViewModel?.onboardingData?.firstName,
                    parentViewModel?.onboardingData?.lastName,
                    parentViewModel?.onboardingData?.countryCode,
                    parentViewModel?.onboardingData?.mobileNo,
                    state.twoWayTextWatcher,
                    parentViewModel?.onboardingData?.passcode,
                    parentViewModel?.onboardingData?.accountType.toString(),
                    token = parentViewModel?.onboardingData?.token
                )
            )) {
                is RetroApiResponse.Success -> {
                    sharedPreferenceManager.save(
                        KEY_IS_USER_LOGGED_IN,
                        true
                    )

                    parentViewModel?.onboardingData?.passcode?.let { passCode ->
                        sharedPreferenceManager.savePassCodeWithEncryption(passCode)
                    } ?: toast(context, "Invalid pass code")

                    trackEvent(SignupEvents.SIGN_UP_EMAIL.type, state.twoWayTextWatcher)
                    sharedPreferenceManager.saveUserNameWithEncryption(state.twoWayTextWatcher)
                    setVerificationLabel()
                    state.setSuccessUI()
                    state.loading = false
                    requestSaveReferral()
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.emailError = response.error.message
                }
            }
        }
    }

    private fun setVerificationLabel() {
        state.emailTitle = getString(R.string.screen_email_verification_display_text_title)
        state.emailBtnTitle = getString(R.string.common_button_next)
        state.deactivateField = false

        val screen_email_verification_b2c_display_text_email_sent: String =
            getString(R.string.screen_email_verification_b2c_display_text_email_sent)
        val screen_email_verification_b2c_display_text_email_confirmation: String =
            getString(R.string.screen_email_verification_b2c_display_text_email_confirmation)
        val screen_email_verification_b2b_display_text_email_sent: String =
            getString(R.string.screen_email_verification_b2b_display_text_email_sent)
        val screen_email_verification_b2b_display_text_email_confirmation: String =
            getString(R.string.screen_email_verification_b2b_display_text_email_confirmation)

        val verificationText: String =
            parentViewModel?.onboardingData?.firstName + ", " + screen_email_verification_b2c_display_text_email_sent + " " + state.twoWayTextWatcher + "\n" + "\n" + screen_email_verification_b2c_display_text_email_confirmation
        state.emailVerificationTitle = verificationText

        // mark that we have completed all verification stuff to handle proper back navigation
        state.verificationCompleted = true
        setProgress(100)
        animationStartEvent.value = true
    }

    override fun sendVerificationEmail() {
        launch {
            state.loading = true
            state.refreshField = true
            when (val response = repository.sendVerificationEmail(
                SendVerificationEmailRequest(
                    state.twoWayTextWatcher,
                    parentViewModel?.onboardingData?.accountType.toString(),
                    parentViewModel?.onboardingData?.token.toString()
                )
            )) {
                is RetroApiResponse.Error -> {
                    state.emailError = response.error.message
                    state.loading = false

                }
                is RetroApiResponse.Success -> {
                    parentViewModel?.onboardingData?.token = response.data.token
                    signUp()
                }
            }
        }
    }

    override fun postDemographicData() {

        val deviceId: String? =
            sharedPreferenceManager.getValueString(KEY_APP_UUID)
        launch {
            state.valid = false
            state.loading = true
            state.refreshField = true
            when (val response = repository.postDemographicData(
                DemographicDataRequest(
                    "SIGNUP",
                    Build.VERSION.RELEASE,
                    deviceId.toString(),
                    Build.BRAND,
                    if (Utils.isEmulator()) "generic" else Build.MODEL,
                    "Android"
                )
            )) {
                is RetroApiResponse.Success -> {
                    getAccountInfo()
                }
                is RetroApiResponse.Error -> {
                    state.valid = true
                    state.loading = false
                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }

    private fun getAccountInfo() {
        launch {
            state.loading = true
            state.refreshField = true
            when (val response = repository.getAccountInfo()) {
                is RetroApiResponse.Success -> {
                    if (response.data.data.isNotEmpty()) {
                        parentViewModel?.onboardingData?.ibanNumber = response.data.data[0].iban
                        delay(500)
                        SessionManager.user = response.data.data[0]
                        SessionManager.setupDataSetForBlockedFeatures()
                        state.valid = true
                        state.loading = false
                        nextButtonPressEvent.setValue(EVENT_NAVIGATE_NEXT)
                    }
                }
                is RetroApiResponse.Error -> {
                    state.valid = true
                    state.toast = response.error.message
                    state.loading = false

                }

            }
        }
    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (state.valid) {
                    handlePressOnNext()
                }
            }
            false
        }
    }

    private fun requestSaveReferral() {
        SharedPreferenceManager(context).getReferralInfo()?.let {
            launch {
                when (val response =
                    repository.saveReferalInvitation(SaveReferalRequest(it.id, it.date))) {

                    is RetroApiResponse.Success -> {
                        SharedPreferenceManager(context).setReferralInfo(null)
                    }
                    is RetroApiResponse.Error -> {
                    }
                }
            }
        }
    }
}