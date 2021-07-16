package co.yap.modules.dashboard.yapit.y2y.home.yapcontacts

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import co.yap.networking.customers.requestdtos.Contact
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.PagingState

interface IYapContact {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun getState(): LiveData<PagingState>
        fun handlePressOnView(id: Int)
        fun retry()
    }

    interface State : IBase.State {
        var listCountDescription: String
    }
}