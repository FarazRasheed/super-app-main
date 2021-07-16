package co.yap.sendmoney.addbeneficiary.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.addbeneficiary.interfaces.IBeneficiaryOverview
import co.yap.sendmoney.addbeneficiary.states.BeneficiaryOverviewState
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent

class BeneficiaryOverviewViewModel(application: Application) :
    SendMoneyBaseViewModel<IBeneficiaryOverview.State>(application), IBeneficiaryOverview.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val repository: CustomersRepository = CustomersRepository

    override val state: BeneficiaryOverviewState = BeneficiaryOverviewState()

    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var beneficiary: Beneficiary= Beneficiary()
    override var onDeleteSuccess: MutableLiveData<Int> = MutableLiveData()


    override fun handlePressOnAddNow(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnConfirm(id: Int) {
        clickEvent.setValue(id)

    }

    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()


    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_edit_beneficiary_display_text_title))
        //toggleAddButtonVisibility(false)
    }

    override fun requestUpdateBeneficiary() {

        launch {
            state.loading = true
            when (val response = repository.editBeneficiary(state.beneficiary!!)) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.toast = response.data.toString()
                    onDeleteSuccess.setValue(222)

                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message

                }
            }
        }
    }
}