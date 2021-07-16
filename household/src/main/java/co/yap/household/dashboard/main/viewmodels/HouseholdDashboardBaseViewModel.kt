package co.yap.household.dashboard.main.viewmodels

import android.app.Application
import co.yap.household.dashboard.main.interfaces.IHouseholdDashboard
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class HouseholdDashboardBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {

    var parentViewModel: IHouseholdDashboard.ViewModel? = null

}