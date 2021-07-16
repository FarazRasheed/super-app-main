package co.yap.household.dashboard.main.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseholdDashboard {
    interface View : IBase.View<ViewModel> {
        fun closeDrawer()
        fun openDrawer()
        fun toggleDrawer()
        fun enableDrawerSwipe(enable: Boolean)
    }

    interface ViewModel : IBase.ViewModel<State>{
        var clickEvent:SingleClickEvent
        fun handlePressOnView(id:Int)
    }
    interface State : IBase.State

}