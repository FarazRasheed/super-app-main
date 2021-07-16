package co.yap.household.dashboard.home.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseholdHome {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        var viewState: MutableLiveData<Int>
        var notificationList: MutableLiveData<ArrayList<Notification>>
        fun handlePressOnView(id: Int)
        fun requestTransactions()
    }

    interface State : IBase.State
}