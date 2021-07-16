package co.yap.modules.onboarding.viewmodels

import android.app.Application
import co.yap.modules.onboarding.interfaces.ICongratulations
import co.yap.modules.onboarding.states.CongratulationsState
import co.yap.networking.cards.CardsRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.yapcore.AdjustEvents.Companion.trackAdjustPlatformEvent
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.adjust.AdjustEvents
import co.yap.yapcore.helpers.extentions.maskIbanNumber
import co.yap.yapcore.managers.SessionManager

class CongratulationsViewModel(application: Application) :
    OnboardingChildViewModel<ICongratulations.State>(application),
    IRepositoryHolder<CardsRepository>,
    ICongratulations.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: CongratulationsState = CongratulationsState()
    override val repository: CardsRepository = CardsRepository
    override var elapsedOnboardingTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        SessionManager.getAccountInfo()
        trackAdjustPlatformEvent(AdjustEvents.SIGN_UP_END.type)
        // calculate elapsed updatedDate for onboarding
        elapsedOnboardingTime = parentViewModel?.onboardingData?.elapsedOnboardingTime ?: 0
        state.nameList[0] = parentViewModel?.onboardingData?.firstName
        parentViewModel?.onboardingData?.ibanNumber?.let {
            state.ibanNumber = it.maskIbanNumber()
        }
    }

    override fun onResume() {
        super.onResume()
        setProgress(100)

    }

    override fun handlePressOnCompleteVerification(id: Int) {
        clickEvent.setValue(id)
    }
}