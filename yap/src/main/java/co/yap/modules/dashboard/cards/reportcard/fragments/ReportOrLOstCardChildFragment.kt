package co.yap.modules.dashboard.cards.reportcard.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.cards.reportcard.activities.ReportLostStolenActivityViewModel
import co.yap.modules.dashboard.cards.reportcard.viewmodels.ReportLostOrStolenCardChildViewModels
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class ReportOrLOstCardChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is ReportLostOrStolenCardChildViewModels<*>) {
            (viewModel as ReportLostOrStolenCardChildViewModels<*>).parentViewModel =
                ViewModelProviders.of(activity!!)
                    .get(ReportLostStolenActivityViewModel::class.java)
        }
    }
}