package co.yap.modules.dashboard.more.cdm

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import co.yap.BR
import co.yap.networking.cards.responsedtos.AtmCdmData
import co.yap.widgets.State
import co.yap.yapcore.BaseState

class CdmMapState : BaseState(), ICdmMap.State {
    override var stateLiveData: MutableLiveData<State> = MutableLiveData()
    override var locationType: MutableLiveData<String>? = MutableLiveData("")

    @get:Bindable
    override var atmCdmData: AtmCdmData? = AtmCdmData()
        set(value) {
            field = value
            notifyPropertyChanged(BR.atmCdmData)
        }
}