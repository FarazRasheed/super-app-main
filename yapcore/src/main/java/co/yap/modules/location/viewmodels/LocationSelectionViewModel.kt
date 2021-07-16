package co.yap.modules.location.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import co.yap.modules.location.interfaces.ILocationSelection
import co.yap.modules.location.states.LocationSelectionState
import co.yap.modules.placesautocomplete.PlaceAPI
import co.yap.modules.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import co.yap.modules.placesautocomplete.model.Place
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.City
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Translator
import co.yap.yapcore.R
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.helpers.StringUtils
import co.yap.yapcore.interfaces.OnItemClickListener

class LocationSelectionViewModel(application: Application) :
    LocationSelectionBaseViewModel<ILocationSelection.State>(application),
    ILocationSelection.ViewModel, IRepositoryHolder<CustomersRepository> {
    override var isUnNamedLocation: Boolean = false
    override var hasSeletedLocation: Boolean = false
    override var unNamed: String = "Unnamed"
    override var defaultHeading: String = ""
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var isMapExpanded: MutableLiveData<Boolean> = MutableLiveData()
    override var termsCheckedTime: MutableLiveData<String> = MutableLiveData("")
    override var cities: MutableLiveData<ArrayList<City>> = MutableLiveData()
    override var selectedPlaceId: MutableLiveData<String> = MutableLiveData("")
    override val state: LocationSelectionState = LocationSelectionState(application)
    override val repository: CustomersRepository = CustomersRepository
    private val cardsRepository: CardsRepository = CardsRepository
    override var address: Address? = null
    override lateinit var placesAdapter: PlacesAutoCompleteAdapter
    override fun onCreate() {
        super.onCreate()
        getCities()
        initializePlacesAdapter()
    }

    override fun handleOnPressView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getCities() {
        launch {
            state.loading = true
            when (val response = repository.getCities()) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let {
                        cities.value = it
                    } ?: showMessage("No data found")
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                }
            }
        }
    }

    fun showMessage(msg: String) {
        state.toast = "$msg^${AlertType.DIALOG.name}"
    }

    override fun onResume() {
        super.onResume()
        state.toolbarVisibility = true
    }

    override fun onLocationSelected() {
        hasSeletedLocation = true

        if (state.placeTitle.get()?.toLowerCase()
                ?.contains(unNamed.toLowerCase()) == true || !StringUtils.isValidAddress(
                state.placeSubTitle.get() ?: ""
            )
            || !StringUtils.isValidAddress(
                state.placeTitle.get() ?: ""
            )
        ) {
            state.headingTitle.set(defaultHeading)
            isUnNamedLocation = true
            state.isUnNamed.set(true)
            state.subHeadingTitle.set(
                Translator.getString(
                    getApplication(),
                    R.string.screen_meeting_location_display_text_add_manual_address_subtitle
                )
            )
            state.placeSubTitle.set("")
            state.addressTitle.set("")
            if (state.placeSubTitle.get()?.toLowerCase()
                    ?.contains(unNamed.toLowerCase()) == true
            ) {
                state.addressSubtitle.set("")

            }
        } else {
            state.isUnNamed.set(false)
            state.addressTitle.set(state.placeSubTitle.get() ?: "")
            state.headingTitle.set(state.placeTitle.get() ?: "")
            state.subHeadingTitle.set(
                Translator.getString(
                    getApplication(),
                    R.string.screen_meeting_location_display_text_selected_subtitle
                )
            )
        }
    }

    override fun requestOrderCard(address: Address?, success: () -> Unit) {
        address?.let {
            // Please confirm weather card name and design code is empty on IOS too or its typo mistake
            it.cardName = ""
            launch {
                state.loading = true
                when (val response = cardsRepository.orderCard(it)) {
                    is RetroApiResponse.Success -> {
                        state.loading = false
                        success()
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    }
                }
            }
        }
    }


    fun getUserAddress(): Address? {
        address?.address1 = state.addressTitle.get()
        address?.address2 = state.addressSubtitle.get()
        address?.city = state.city.get()
        // this needs to be update and addresse title 1,2,3 should remove only addresse object will pass and recived.
        address?.nearestLandMark = state.addressTitle.get()
        address?.country = "United Arab Emirates"
        return address
    }

    fun isValidAddress(): Boolean {
        if (!StringUtils.isValidAddress(
                state.addressTitle.get() ?: ""
            ) || !StringUtils.isValidAddress(state.addressSubtitle.get() ?: "")
        ) {
            showToast("Invalid address found")
            return false
        }
        return true
    }

    val autoCompleteListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Place) {
                state.placeTitle.set(data.mainText)
                state.placeSubTitle.set(data.description)
                state.isLocationInAllowedCountry.set(true)
                selectedPlaceId.value = data.id
            }
        }
    }

    private fun initializePlacesAdapter() {
        val placeAPI = PlaceAPI.Builder().apiKey(context.getString(R.string.google_maps_key)).build(context)
        placesAdapter = PlacesAutoCompleteAdapter(context, placeAPI)
    }
}
