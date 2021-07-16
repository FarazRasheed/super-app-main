package co.yap.household.onboard.onboarding.viewmodels

import android.app.Application
import co.yap.household.R
import co.yap.household.onboard.onboarding.interfaces.INewUserSuccess
import co.yap.household.onboard.onboarding.states.NewUserCongratulationsState
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingChildViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class NewUserSuccessViewModel(application: Application) :
    OnboardingChildViewModel<INewUserSuccess.State>(application),
    INewUserSuccess.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: NewUserCongratulationsState = NewUserCongratulationsState()
    override var elapsedOnboardingTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        // calculate elapsed updatedDate for onboarding
        elapsedOnboardingTime = parentViewModel?.onboardingData?.elapsedOnboardingTime ?: 0
        state.nameList[0] = parentViewModel?.onboardingData?.firstName
        state.heading =
            getString(Strings.screen_congratulations_display_text_title).format(parentViewModel?.state?.accountInfo?.currentCustomer?.firstName)
        parentViewModel?.onboardingData?.ibanNumber?.let {
            state.ibanNumber = maskIbanNumber(it.trim())
        }
        // state.ibanNumber = maskIbanNumber(parentViewModel?.onboardingData.ibanNumber?.trim())
    }

    override fun onResume() {
        super.onResume()
//        updateBackground(R.color.colorLightPinkBackground)
        updateBackground(context.resources.getColor(R.color.colorLightPinkBackground))

//        updateBackground(Color.WHITE)


    }

    override fun handlePressOnCompleteVerification(id: Int) {
        clickEvent.setValue(id)
    }

    private fun maskIbanNumber(unmaskedIban: String): String {


        return if (unmaskedIban.length >= 8) {
            val firstPartIban: String = unmaskedIban.substring(0, 2)
            val secondPartIban: String = unmaskedIban.substring(2, 4)
            val thirdPartIban: String = unmaskedIban.substring(4, 7)
            val fourthPartIban: String = unmaskedIban.substring(7, unmaskedIban.length - 6)
            "$firstPartIban $secondPartIban $thirdPartIban $fourthPartIban******"
        } else {
            unmaskedIban
        }
    }


}