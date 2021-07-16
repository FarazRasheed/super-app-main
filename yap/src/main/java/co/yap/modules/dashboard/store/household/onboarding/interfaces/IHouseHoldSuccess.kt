package co.yap.modules.dashboard.store.household.onboarding.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldSuccess {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnShare(id: Int)
        val clickEvent: SingleClickEvent
        fun handlePressOnGoBackToDashBoard(id: Int)
    }

    interface State : IBase.State {
        var houseHoldUserName: String
        var houseHoldUserEmail: String
        var houseHoldUserMobile: String
        var houseHoldUserPassCode: String
        var houseHoldDescription: String
    }
}