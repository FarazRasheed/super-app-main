package co.yap.modules.onboarding.states

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import co.yap.modules.onboarding.interfaces.ICongratulations
import co.yap.yapcore.BaseState

class CongratulationsState : BaseState(), ICongratulations.State {

    override val nameList: Array<String?> = arrayOfNulls(1)

    @get:Bindable
    override var ibanNumber: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ibanNumber)
        }


    @get:Bindable
    override var onboardingTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.onboardingTime)
        }

}