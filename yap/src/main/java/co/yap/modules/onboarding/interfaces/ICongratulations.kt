package co.yap.modules.onboarding.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICongratulations {

    interface View : IBase.View<ViewModel>{
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var elapsedOnboardingTime: Long
        fun handlePressOnCompleteVerification(id: Int)
    }

    interface State : IBase.State {
        val nameList: Array<String?>
        // var name: String
        var ibanNumber: String
        var onboardingTime: String
    }
}