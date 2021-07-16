package co.yap.household.onboard.onboarding.main.states

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import co.yap.household.R
import co.yap.household.onboard.onboarding.main.interfaces.IOnboarding
import co.yap.networking.customers.responsedtos.AccountInfo
import co.yap.yapcore.BaseState

class OnBoardingState(val application: Application) : BaseState(), IOnboarding.State {

    @get:Bindable
    override var currentBackground: Int = application.resources.getColor(R.color.white)
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentBackground)
        }

    @get:Bindable
    override var totalProgress: Int = 100
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalProgress)
        }

    @get:Bindable
    override var currentProgress: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentProgress)
        }
    @get:Bindable
    override var existingYapUser: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.existingYapUser)
        }
    @get:Bindable
    override var accountInfo: AccountInfo? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountInfo)
        }
}