package co.yap.modules.dashboard.more.home.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.home.interfaces.IMoreHome
import co.yap.yapcore.BaseState

class MoreState : BaseState(), IMoreHome.State {

    override var image: ObservableField<String> = ObservableField("")
    override var initials: ObservableField<String> = ObservableField("")
}