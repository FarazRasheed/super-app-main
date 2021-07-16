package co.yap.modules.location.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.databinding.FragmentPlaceOfBirthSelectionBinding


interface IPOBSelection {

    interface View : IBase.View<ViewModel> {
        fun addObservers()
        fun removeObservers()
        fun getBinding(): FragmentPlaceOfBirthSelectionBinding
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handleOnPressView(id: Int)
        fun saveDOBInfo(success: () -> Unit)
        var clickEvent: SingleClickEvent
        var populateSpinnerData: MutableLiveData<ArrayList<Country>>
    }

    interface State : IBase.State {
        var cityOfBirth: String
        var valid: ObservableField<Boolean>
        var selectedCountry: Country?
        var selectedCountryIndex: Int
    }
}