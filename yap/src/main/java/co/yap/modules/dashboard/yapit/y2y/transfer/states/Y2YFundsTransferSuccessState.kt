package co.yap.modules.dashboard.yapit.y2y.transfer.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.yapit.y2y.transfer.interfaces.IY2YFundsTransferSuccess
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager


class Y2YFundsTransferSuccessState : BaseState(), IY2YFundsTransferSuccess.State {

    @get:Bindable
    override var transferredAmount: String = "${SessionManager.getDefaultCurrency()} 500"
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferredAmount)
        }
    @get:Bindable
    override var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    override var imageUrl: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.imageUrl)
        }
}