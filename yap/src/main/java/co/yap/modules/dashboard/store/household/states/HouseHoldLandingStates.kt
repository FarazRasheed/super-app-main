package co.yap.modules.dashboard.store.household.states

import androidx.databinding.ObservableBoolean
import co.yap.modules.dashboard.store.household.interfaces.IHouseHoldLanding
import co.yap.yapcore.BaseState

class HouseHoldLandingStates : BaseState(), IHouseHoldLanding.State {

    override var rightIcon: ObservableBoolean = ObservableBoolean(false)
    override var leftIcon: ObservableBoolean = ObservableBoolean(false)
    override var toolbarVisibility: ObservableBoolean = ObservableBoolean(false)
}