package co.yap.modules.dashboard.cards.reportcard.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.reportcard.interfaces.IReportStolenActivity
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class ReportLostOrStolenCardChildViewModels<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IReportStolenActivity.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleReportCardToolBarVisibility(visibility: Boolean) {
        val VISIBLE: Int = 0x00000000
        val GONE: Int = 0x00000008
        if (visibility) {
            parentViewModel?.state?.tootlBarVisibility = VISIBLE

        } else {
            parentViewModel?.state?.tootlBarVisibility = GONE

        }
    }
}
