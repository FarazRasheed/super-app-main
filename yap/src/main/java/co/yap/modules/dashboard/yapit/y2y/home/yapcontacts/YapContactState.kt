package co.yap.modules.dashboard.yapit.y2y.home.yapcontacts

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.yapit.y2y.home.interfaces.IYapToYap
import co.yap.yapcore.BaseState

class YapContactState : BaseState(), IYapContact.State {
    @get:Bindable
    override var listCountDescription: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.listCountDescription)
        }

}