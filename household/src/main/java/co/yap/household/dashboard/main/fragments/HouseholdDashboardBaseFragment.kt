package co.yap.household.dashboard.main.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.household.dashboard.main.activities.HouseholdDashboardActivity
import co.yap.household.dashboard.main.interfaces.IHouseholdDashboard
import co.yap.household.dashboard.main.viewmodels.HouseholdDashboardBaseViewModel
import co.yap.household.dashboard.main.viewmodels.HouseholdDashboardViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class HouseholdDashboardBaseFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    protected val parentView: IHouseholdDashboard.View?
        get() = (activity as HouseholdDashboardActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is HouseholdDashboardBaseViewModel<*>) {
            (viewModel as HouseholdDashboardBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(HouseholdDashboardViewModel::class.java)
        }
    }
}