package co.yap.modules.dashboard.store.household.onboarding.viewmodels

import android.app.Application
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IBaseOnboarding
import co.yap.modules.dashboard.store.household.onboarding.states.BaseOnboardingState
import co.yap.networking.household.responsedtos.HouseHoldPlan
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent

class HouseHoldOnboardingViewModel(application: Application) :
    BaseViewModel<IBaseOnboarding.State>(application),
    IBaseOnboarding.ViewModel {

    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: BaseOnboardingState = BaseOnboardingState()
    override var firstName: String = ""
    override var lastName: String = ""
    override var username: String = ""
    override var userMobileNo: String = ""
    override var countryCode: String = "00971"
    override var tempPasscode: String = "0000"
    override var selectedPlanType: HouseHoldPlan = HouseHoldPlan()
    override var plansList: ArrayList<HouseHoldPlan> = ArrayList()
    override fun handlePressOnBackButton() {
        backButtonPressEvent.value = true
    }

    override fun handlePressOnTickButton() {

    }
}