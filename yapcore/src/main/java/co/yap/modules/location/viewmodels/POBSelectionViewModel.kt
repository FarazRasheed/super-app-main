package co.yap.modules.location.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.modules.location.interfaces.IPOBSelection
import co.yap.modules.location.states.POBSelectionState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.BirthInfoRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.Utils

class POBSelectionViewModel(application: Application) :
    LocationChildViewModel<IPOBSelection.State>(application),
    IPOBSelection.ViewModel, IRepositoryHolder<CustomersRepository> {
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: IPOBSelection.State = POBSelectionState()
    override var populateSpinnerData: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    override val repository: CustomersRepository = CustomersRepository

    override fun handleOnPressView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        getAllCountries()
    }




    private fun getAllCountries() {
        if (!parentViewModel?.countries.isNullOrEmpty()) {
            populateSpinnerData.setValue(parentViewModel?.countries)
        } else {
            launch {
                state.loading = true
                when (val response = repository.getAllCountries()) {
                    is RetroApiResponse.Success -> {
                        populateSpinnerData.value = Utils.parseCountryList(response.data.data,addOIndex = false)
                        parentViewModel?.countries = populateSpinnerData.value as ArrayList<Country>
                        state.loading = false
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = response.error.message
                    }
                }
            }
        }
    }

    override fun saveDOBInfo(success: () -> Unit) {
        launch {
            state.loading = true
            when (val response = repository.saveBirthInfo(
                BirthInfoRequest(
                    countryOfBirth = state.selectedCountry?.getName() ?: "",
                    cityOfBirth = state.cityOfBirth
                )
            )) {
                is RetroApiResponse.Success -> {
                    success.invoke()
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                }
            }
        }
    }
}