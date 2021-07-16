package co.yap.modules.dashboard.home.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.BR
import co.yap.modules.dashboard.home.interfaces.IYapHome
import co.yap.widgets.State
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class YapHomeState : BaseState(), IYapHome.State {

    @get:Bindable
    override var availableBalance: String? = SessionManager.cardBalance.value?.availableBalance
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalance)
        }
    override var filterCount: ObservableField<Int> = ObservableField()
    override var isTransEmpty: ObservableField<Boolean> = ObservableField(true)
    override var showTxnShimmer: MutableLiveData<State> = MutableLiveData()
    override var isUserAccountActivated: ObservableField<Boolean> = ObservableField(true)
}