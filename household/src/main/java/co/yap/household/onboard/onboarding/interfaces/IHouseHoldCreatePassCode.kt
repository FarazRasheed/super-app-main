package co.yap.household.onboard.onboarding.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldCreatePassCode {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent?
        var onPasscodeSuccess: MutableLiveData<Boolean>
        fun handlePressOnCreatePasscodeButton(id: Int)
        fun createPassCodeRequest()
    }

    interface State : IBase.State {
        var passcode: String
        var dialerError: String
        var buttonValidation: Boolean
    }
}