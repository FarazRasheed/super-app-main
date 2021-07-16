package co.yap.modules.dashboard.cards.reportcard.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.reportcard.interfaces.IReportOrLostBase
import co.yap.modules.dashboard.cards.reportcard.states.ReportOrLostStolenCardState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent

class ReportLostOrStolenCardBaseViewModels(application: Application) :
    BaseViewModel<IReportOrLostBase.State>(application),
    IReportOrLostBase.ViewModel {

    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: ReportOrLostStolenCardState =
        ReportOrLostStolenCardState()

    override fun handlePressOnBackButton() {
        backButtonPressEvent.value = true
    }
}