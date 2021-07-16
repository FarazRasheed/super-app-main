package co.yap.modules.dashboard.store.interfaces

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.PagingState

interface IYapStore {

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIconVisibility: ObservableBoolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val storesLiveData: MutableLiveData<MutableList<Store>>
        fun handlePressOnView(id: Int)
        fun getStoreList()
    }

    interface View : IBase.View<ViewModel>
}