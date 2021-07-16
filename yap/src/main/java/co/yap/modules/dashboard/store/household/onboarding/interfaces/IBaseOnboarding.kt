package co.yap.modules.dashboard.store.household.onboarding.interfaces

import co.yap.networking.household.responsedtos.HouseHoldPlan
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent

interface IBaseOnboarding {

    interface State : IBase.State {
        var tootlBarTitle: String
        var tootlBarVisibility: Int
    }

    interface ViewModel : IBase.ViewModel<State> {
        var firstName:String
        var lastName:String
        var username: String
        var userMobileNo: String
        var countryCode: String
        var tempPasscode: String
        var selectedPlanType: HouseHoldPlan
        var plansList: ArrayList<HouseHoldPlan>
        fun handlePressOnBackButton()
        fun handlePressOnTickButton()
        val backButtonPressEvent: SingleLiveEvent<Boolean>

    }

    interface View : IBase.View<ViewModel>
}