package co.yap.modules.dashboard.cards.status.state

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.status.interfaces.IYapCardStatus
import co.yap.yapcore.BaseState

class YapCardStatusState : BaseState(), IYapCardStatus.State {

    override val message: ObservableField<String> = ObservableField()
    override val title: ObservableField<String> = ObservableField()
    override val cardType: ObservableField<String> = ObservableField()

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var buildingProgress: Int = 100
        set(value) {
            field = value
            notifyPropertyChanged(BR.buildingProgress)
        }

    @get:Bindable
    override var shippingProgress: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.shippingProgress)
        }

    @get:Bindable
    override var totalProgress: Int = 100
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalProgress)
        }
}