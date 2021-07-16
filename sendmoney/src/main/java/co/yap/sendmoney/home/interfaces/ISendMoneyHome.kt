package co.yap.sendmoney.home.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.currency.CurrenciesResponse
import co.yap.networking.customers.responsedtos.currency.CurrencyData
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.home.adapters.RecentTransferAdaptor
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.PagingState

interface ISendMoneyHome {
    interface State : IBase.State {
        var isNoBeneficiary: ObservableField<Boolean>
        var hasBeneficiary: ObservableField<Boolean>
        var isNoRecentBeneficiary: ObservableField<Boolean>
        var isSearching: ObservableField<Boolean>
        var flagDrawableResId: ObservableField<Int>
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        var pagingState: MutableLiveData<PagingState>
        val allBeneficiariesLiveData: MutableLiveData<List<Beneficiary>>
        val onDeleteSuccess: MutableLiveData<Int>
        val recentTransferData: MutableLiveData<List<Beneficiary>>
        val adapter: ObservableField<RecentTransferAdaptor>
        fun handlePressOnView(id: Int)
        fun requestDeleteBeneficiary(beneficiaryId: Int)
        fun requestRecentBeneficiaries()
        fun requestAllBeneficiaries()
        fun getState(): LiveData<PagingState>
        val searchQuery: MutableLiveData<String>
        val isSearching: MutableLiveData<Boolean>
    }

    interface View : IBase.View<ViewModel>
}