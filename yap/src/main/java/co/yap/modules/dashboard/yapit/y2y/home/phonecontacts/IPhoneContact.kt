package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.requestdtos.Contact
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.PagingState

interface IPhoneContact {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val phoneContactLiveData: MutableLiveData<List<Contact>>
        fun getState(): LiveData<PagingState>
        fun handlePressOnView(id: Int)
        fun getY2YBeneficiaries()
        fun listIsEmpty(): Boolean
    }

    interface State : IBase.State {

    }
}