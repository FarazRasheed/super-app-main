package co.yap.household.onboard.onboarding.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldNumberRegistration {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun populateState()
        var clickEvent: SingleClickEvent?
        fun handlePressOnConfirm(id: Int)
        fun verifyHouseholdParentMobile()
        var isParentMobileValid:MutableLiveData<Boolean>?
    }

    interface State : IBase.State {
        var welcomeHeading: String
        var numberConfirmationValue: String
        var parentName: String?
        var userName: String?
        var phoneNumber: String?
        var countryCode: String
        var buttonTitle: String
        var buttonValidation: Boolean
        var showErrorMessage: Boolean
        var existingYapUser: Boolean?
    }
}