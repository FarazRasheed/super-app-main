package co.yap.modules.dashboard.store.states

import androidx.databinding.ObservableBoolean
import co.yap.modules.dashboard.store.interfaces.IYapStore
import co.yap.yapcore.BaseState

class YapStoreState : BaseState(), IYapStore.State {
    override var toolbarVisibility: ObservableBoolean = ObservableBoolean()
    override var rightIconVisibility: ObservableBoolean = ObservableBoolean()
}
