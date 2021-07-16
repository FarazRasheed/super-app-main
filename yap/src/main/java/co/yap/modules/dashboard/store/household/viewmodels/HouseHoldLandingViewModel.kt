package co.yap.modules.dashboard.store.household.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.modules.dashboard.store.household.interfaces.IHouseHoldLanding
import co.yap.modules.dashboard.store.household.states.HouseHoldLandingStates
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class HouseHoldLandingViewModel(application: Application) :
    BaseViewModel<IHouseHoldLanding.State>(application),
    IHouseHoldLanding.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: HouseHoldLandingStates = HouseHoldLandingStates()
    override var selectedCountry: MutableLiveData<Country> = MutableLiveData(Country())
    override var transferType: MutableLiveData<String> = MutableLiveData("")
    override var beneficiary: MutableLiveData<Beneficiary> = MutableLiveData(Beneficiary())

    override fun handlePressOnCloseIcon(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnGetHouseHoldCard(id: Int) {
        clickEvent.setValue(id)
    }

}