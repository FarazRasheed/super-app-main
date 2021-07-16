package co.yap.modules.location.states

import androidx.databinding.ObservableBoolean
import co.yap.modules.location.interfaces.ILocation
import co.yap.yapcore.BaseState

class LocationState : BaseState(), ILocation.State {

    override var rightIcon: ObservableBoolean = ObservableBoolean(false)
    override var leftIcon: ObservableBoolean = ObservableBoolean(false)
    override var toolbarVisibility: ObservableBoolean = ObservableBoolean(false)
}