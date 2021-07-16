package co.yap.modules.dashboard.yapit.topup.addtopupcard.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.yapit.topup.addtopupcard.interfaces.IAddTopUpCard
import co.yap.yapcore.BaseState

class AddTopUpCardState : BaseState(), IAddTopUpCard.State {

    @get:Bindable
    override var url: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.url)
        }

    override var toolbarVisibility: ObservableField<Boolean> = ObservableField()
}