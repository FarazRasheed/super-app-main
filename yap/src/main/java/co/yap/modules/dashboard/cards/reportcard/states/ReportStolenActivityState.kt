package co.yap.modules.dashboard.cards.reportcard.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.reportcard.interfaces.IReportStolenActivity
import co.yap.yapcore.BaseState

class ReportStolenActivityState : BaseState(), IReportStolenActivity.State {

    @get:Bindable
    override var tootlBarVisibility: Int = 0x00000000
        set(value) {
            field = value
            notifyPropertyChanged(BR.tootlBarVisibility)

        }
}