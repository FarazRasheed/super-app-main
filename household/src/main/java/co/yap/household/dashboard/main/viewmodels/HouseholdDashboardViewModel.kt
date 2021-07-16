package co.yap.household.dashboard.main.viewmodels

import android.app.Application
import co.yap.household.dashboard.main.interfaces.IHouseholdDashboard
import co.yap.household.dashboard.main.states.HouseholdDashboardState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent


class HouseholdDashboardViewModel(application: Application) :
    BaseViewModel<IHouseholdDashboard.State>(application), IHouseholdDashboard.ViewModel {
    override val state: HouseholdDashboardState =
        HouseholdDashboardState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }
}