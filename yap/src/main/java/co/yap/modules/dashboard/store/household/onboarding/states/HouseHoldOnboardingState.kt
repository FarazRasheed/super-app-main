package co.yap.modules.dashboard.store.household.onboarding.states

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldOnboarding
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class HouseHoldOnboardingState : BaseState(), IHouseHoldOnboarding.State {

    @get:Bindable
    override var monthlyFee: String = "${SessionManager.getDefaultCurrency()} 59.99"
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthlyFee)

        }

    @get:Bindable
    override var annuallyFee: String = "${SessionManager.getDefaultCurrency()} 720.00"
        set(value) {
            field = value
            notifyPropertyChanged(BR.annuallyFee)

        }

    @get:Bindable
    override var subscriptionFee: String = "${SessionManager.getDefaultCurrency()} 19.99/month"
        set(value) {
            field = value
            notifyPropertyChanged(BR.subscriptionFee)

        }
}