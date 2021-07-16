package co.yap.modules.location.interfaces

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import co.yap.modules.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.customers.responsedtos.City
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface ILocationSelection {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun getBinding(): ViewDataBinding
    }

    interface ViewModel : IBase.ViewModel<State> {
        var isUnNamedLocation: Boolean
        var hasSeletedLocation: Boolean
        var unNamed: String
        var defaultHeading: String
        var clickEvent: SingleClickEvent
        var isMapExpanded: MutableLiveData<Boolean>
        var selectedPlaceId: MutableLiveData<String>
        var termsCheckedTime: MutableLiveData<String>
        var cities: MutableLiveData<ArrayList<City>>
        var address: Address?
        fun onLocationSelected()
        fun handleOnPressView(id: Int)
        fun getCities()
        fun requestOrderCard(address: Address?, success: () -> Unit)
        val placesAdapter: PlacesAutoCompleteAdapter
    }

    interface State : IBase.State {
        var isUnNamed: ObservableField<Boolean>
        var toolbarVisibility: Boolean
        var isShowLocationCard: ObservableField<Boolean>
        var headingTitle: ObservableField<String>
        var subHeadingTitle: ObservableField<String>
        var city: ObservableField<String>
        var placeTitle: ObservableField<String>
        var placeSubTitle: ObservableField<String>
        var placePhoto: ObservableField<Bitmap>
        var addressTitle: ObservableField<String>
        var addressSubtitle: ObservableField<String>
        var isTermsChecked: ObservableField<Boolean>
        var valid: ObservableField<Boolean>
        var showTermsCondition: ObservableField<Boolean>
        var isLocationInAllowedCountry: ObservableField<Boolean>
        var isOnBoarding: ObservableField<Boolean>
    }
}