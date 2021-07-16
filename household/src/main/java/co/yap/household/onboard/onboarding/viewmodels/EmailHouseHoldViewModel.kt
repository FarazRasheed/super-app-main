package co.yap.household.onboard.onboarding.viewmodels

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import co.yap.household.R
import co.yap.household.onboard.onboarding.interfaces.IEmail
import co.yap.household.onboard.onboarding.states.EmailState
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingChildViewModel
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.AddHouseholdEmailRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent
import java.util.*
import java.util.concurrent.TimeUnit

class EmailHouseHoldViewModel(application: Application) :
    OnboardingChildViewModel<IEmail.State>(application), IEmail.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val state: EmailState = EmailState(application)
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var hasDoneAnimation: Boolean= false
    override var onEmailVerifySuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override val animationStartEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    override val repository: CustomersRepository = CustomersRepository

    override fun onResume() {
        super.onResume()
        setProgress(80)

    }

    override fun onCreate() {
        super.onCreate()
        state.emailTitle = getString(R.string.screen_new_user_email_display_text_title)
        state.emailBtnTitle = getString(R.string.screen_new_user_email_display_button_confirm)
        state.deactivateField = true
        state.emailVerificationTitle =
            getString(R.string.screen_new_user_email_display_text_email_caption)

    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun stopTimer() {
        parentViewModel?.onboardingData?.elapsedOnboardingTime =
            TimeUnit.MILLISECONDS.toSeconds(
                Date().time - (parentViewModel?.onboardingData?.startTime?.time ?: Date().time)
            )
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
//        state.emailVerificationTitle = verificationText
        state.emailVerificationTitle =
            getString(R.string.screen_new_user_email_display_text_email_caption)


        // mark that we have completed all verification stuff to handle proper back navigation
        state.verificationCompleted = true
        setProgress(100)
        animationStartEvent.value = true
    }

    override fun sendVerificationEmail() {
        launch {
            state.loading = true
            state.refreshField = true
            when (val response =
                repository.addHouseholdEmail(AddHouseholdEmailRequest(state.twoWayTextWatcher))) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    setVerificationLabel()
                    onEmailVerifySuccess.value = true
                }
                is RetroApiResponse.Error -> {
                    state.emailError = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun postDemographicData() {
        //todo call for demographic on backend verdict pending
    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (state.valid) {
                }
            }
            false
        }
    }
}