package co.yap.sendmoney.home.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.home.adapters.RecentTransferAdaptor
import co.yap.sendmoney.home.interfaces.ISendMoneyHome
import co.yap.sendmoney.home.states.SendMoneyHomeState
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.managers.SessionManager


class SendMoneyHomeScreenViewModel(application: Application) :
    SendMoneyBaseViewModel<ISendMoneyHome.State>(application), ISendMoneyHome.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val repository: CustomersRepository = CustomersRepository
    override val state: SendMoneyHomeState = SendMoneyHomeState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var pagingState: MutableLiveData<PagingState> = MutableLiveData()
    override val allBeneficiariesLiveData: MutableLiveData<List<Beneficiary>> = MutableLiveData()
    override var onDeleteSuccess: MutableLiveData<Int> = MutableLiveData()
    override var recentTransferData: MutableLiveData<List<Beneficiary>> = MutableLiveData()
    override val adapter = ObservableField<RecentTransferAdaptor>()
    override val searchQuery: MutableLiveData<String> = MutableLiveData()
    override val isSearching: MutableLiveData<Boolean> = MutableLiveData()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        requestAllBeneficiaries()
        SessionManager.getCurrenciesFromServer { _, _ -> }
        isSearching.value?.let {
            if (!it)
                requestRecentBeneficiaries()
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_send_money_display_text_title))
    }


    override fun getState(): LiveData<PagingState> {
        return pagingState
    }

    override fun requestAllBeneficiaries() {
        launch {
            state.loading = true
            when (val response = repository.getAllBeneficiaries()) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    allBeneficiariesLiveData.value = response.data.data
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }

    override fun requestRecentBeneficiaries() {
        launch {
            when (val response = repository.getRecentBeneficiaries()) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    if (response.data.data.isNullOrEmpty())
                        state.isNoRecentBeneficiary.set(true)
                    else
                        state.isNoRecentBeneficiary.set(false)

                    recentTransferData.value = response.data.data

                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"

                }
            }
        }
    }

    override fun requestDeleteBeneficiary(beneficiaryId: Int) {
        launch {
            state.loading = true
            when (val response = repository.deleteBeneficiaryFromList(beneficiaryId.toString())) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.toast = "Deleted Successfully"
                    requestRecentBeneficiaries()
                    onDeleteSuccess.setValue(111)
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }
}