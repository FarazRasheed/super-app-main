package co.yap.modules.dashboard.store.household.interfaces

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldLanding {
    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIcon: ObservableBoolean
        var leftIcon: ObservableBoolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var selectedCountry: MutableLiveData<Country>
        var transferType: MutableLiveData<String>
        var beneficiary: MutableLiveData<Beneficiary>

        fun handlePressOnCloseIcon(id: Int)
        fun handlePressOnGetHouseHoldCard(id: Int)

    }

    interface View : IBase.View<ViewModel>

}