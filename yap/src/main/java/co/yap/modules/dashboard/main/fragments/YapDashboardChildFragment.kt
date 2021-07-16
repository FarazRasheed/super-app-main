package co.yap.modules.dashboard.main.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.main.activities.YapDashboardActivity
import co.yap.modules.dashboard.main.interfaces.IYapDashboard
import co.yap.modules.dashboard.main.viewmodels.YapDashBoardViewModel
import co.yap.modules.dashboard.main.viewmodels.YapDashboardChildViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class YapDashboardChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    protected val parentView: IYapDashboard.View?
        get() = (activity as YapDashboardActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is YapDashboardChildViewModel<*>) {
            (viewModel as YapDashboardChildViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(YapDashBoardViewModel::class.java)
        }
    }

}