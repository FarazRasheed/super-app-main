package co.yap.household.onboard.onboarding.main.viewmodels

import android.app.Application
import co.yap.household.onboard.onboarding.main.OnboardingData
import co.yap.household.onboard.onboarding.main.interfaces.IOnboarding
import co.yap.household.onboard.onboarding.main.states.OnBoardingState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent


class OnboardingHouseHoldViewModel(application: Application) : BaseViewModel<IOnboarding.State>(application),
    IOnboarding.ViewModel {

    override var onboardingData: OnboardingData =
        OnboardingData(
            "",
            "",
            "",
            "",
            "",
            "",
            "B2C_ACCOUNT",
            "",
            ""
        )
    override val state: OnBoardingState =
        OnBoardingState(application)
    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    override fun handlePressOnBackButton() {
        backButtonPressEvent.value = true
    }

    override fun handlePressOnTickButton() {

    }
}