package co.yap.modules.location.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.location.fragments.LocationBaseFragment
import co.yap.modules.location.interfaces.ILocationSelection
import co.yap.modules.location.viewmodels.LocationSelectionViewModel
import co.yap.yapcore.BR
import co.yap.yapcore.R
import co.yap.yapcore.helpers.permissions.PermissionHelper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

open class MapSupportFragment : LocationBaseFragment<ILocationSelection.ViewModel>() {

    private var mMap: GoogleMap? = null
    private var defaultZoom = 15
    private var placesClient: PlacesClient? = null
    private var icon: BitmapDescriptor? = null
    protected var mDefaultLocation: LatLng? = null
    private var markerOptions: MarkerOptions? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationMarker: Marker? = null
    protected var permissionHelper: PermissionHelper? = null
    private var defaultPlacePhoto: Bitmap? = null
    var isFromPlacesApi: Boolean = false

    override val viewModel: LocationSelectionViewModel
        get() = ViewModelProviders.of(this).get(LocationSelectionViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.location_selection_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        icon = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_location_pin)
        defaultPlacePhoto = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.location_place_holder
        )
    }

    fun getCurrentLocation() {
        mFusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                mDefaultLocation = LatLng(location.latitude, location.longitude)
                viewModel.address?.latitude = location.latitude
                viewModel.address?.longitude = location.longitude

                setupMapOptions()
            } else {
                startLocationUpdates()
            }
        }
    }

    private fun initMap() {
        val apiKey = getString(R.string.google_maps_key)
        Places.initialize(requireContext(), apiKey)
        placesClient = Places.createClient(requireContext())
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        //getCurrentLocation()
    }

    protected fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mMap = googleMap
            if (mDefaultLocation != null) {
                setupMapOptions()
            } else {
                getCurrentLocation()
            }
        }
    }

    private fun setupMapOptions() {
        createMarker(mDefaultLocation)
        mMap?.uiSettings?.isZoomControlsEnabled = false
        mMap?.uiSettings?.isMapToolbarEnabled = false
        mMap?.uiSettings?.isCompassEnabled = false
        mDefaultLocation?.let {
            animateCameraToLocation(it)
        }
        mMap?.setOnMapClickListener {
            it?.let { latLng ->
                isFromPlacesApi = false
                mDefaultLocation = latLng
                createMarker(latLng)
                loadAysnMapInfo(latLng)
            }
        }
    }

    protected fun loadAysnMapInfo(latLng: LatLng) {
        viewModel.launch {
            val address = viewModel.viewModelBGScope.async(Dispatchers.IO) {
                getSelectedMapLocation(latLng)
            }
            populateCardState(address.await())
        }

    }

    private fun getSelectedMapLocation(location: LatLng): co.yap.networking.cards.responsedtos.Address? {
        try {
            val geoCoder = Geocoder(requireContext())
            val list = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            val selectedAddress: Address = list[0]
            val locality = selectedAddress.locality
            val countryName = selectedAddress.countryName
            var placeName =
                selectedAddress.getAddressLine(0).split(",").toTypedArray()[0]
            val placeSubTitle = selectedAddress.getAddressLine(0)
            if (placeName == placeSubTitle) placeName = selectedAddress.featureName
            viewModel.state.isLocationInAllowedCountry.set(selectedAddress.countryCode == "AE")
            return co.yap.networking.cards.responsedtos.Address(
                address1 = placeName,
                address2 = placeSubTitle,
                latitude = location.latitude,
                longitude = location.longitude,
                city = locality,
                country = countryName
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun populateCardState(
        address: co.yap.networking.cards.responsedtos.Address?,
        populatePassingAddress: Boolean = false
    ) {
        if (populatePassingAddress) {
            fillAddress(address, populatePassingAddress)
        } else {
            if (viewModel.state.isLocationInAllowedCountry.get() == true) {
                viewModel.state.isShowLocationCard.set(true)
                fillAddress(address)
            } else
                showNotAllowedError()
        }
    }

    fun fillAddress(
        address: co.yap.networking.cards.responsedtos.Address?,
        populatePassingAddress: Boolean = false
    ) {
        address?.let { _address ->
            viewModel.address?.latitude = _address.latitude
            viewModel.address?.longitude = _address.longitude
            viewModel.state.addressSubtitle.set("")
            viewModel.state.placeTitle.set(_address.address1)
            viewModel.state.placeSubTitle.set(_address.address2)
            viewModel.state.placePhoto.set(defaultPlacePhoto)
            if (populatePassingAddress) {
                viewModel.state.addressSubtitle.set(viewModel.address?.address2)
                _address.city?.let { viewModel.state.city.set(_address.city) }
                    ?: viewModel.state.city.set("Select")
            } else {
                val cityMatched =
                    viewModel.cities.value?.firstOrNull { it.name.equals(_address.city, true) }
                cityMatched?.let {
                    viewModel.state.city.set(it.name)
                } ?: viewModel.state.city.set("Select")
            }
        }
    }

    private fun showNotAllowedError() {
        viewModel.state.isShowLocationCard.set(false)
        viewModel.state.toast = "Your location must be in the UAE."
    }

    private fun createMarker(markerLatLng: LatLng?) {
        viewModel.isUnNamedLocation = false
        locationMarker?.remove()
        //icon = bitmapDescriptorFromVector(context, R.drawable.ic_location_pin)
        markerLatLng?.let {
            markerOptions = MarkerOptions()
                .icon(icon)
                .position(it)
            locationMarker = mMap?.addMarker(markerOptions)
        }

    }

    protected fun animateCameraToLocation(location: LatLng) {
        createMarker(location)
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(defaultZoom.toFloat()).build()
        mMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition)
        )
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun attemptFetchPhoto(place: Place) {
        val photoMetaData = place.photoMetadatas
        if (photoMetaData != null && photoMetaData.isNotEmpty()) {
            fetchPhoto(photoMetaData[0])
        }
    }

    private fun fetchPhoto(photoMetadata: PhotoMetadata) {
        val photoRequestBuilder = FetchPhotoRequest.builder(photoMetadata)

        val photoTask = placesClient?.fetchPhoto(photoRequestBuilder.build())

        photoTask?.addOnSuccessListener { response ->
            viewModel.state.placePhoto.set(response.bitmap)
        }

        photoTask?.addOnFailureListener { exception ->
            exception.printStackTrace()
            viewModel.state.loading = false
        }

        photoTask?.addOnCompleteListener {
            viewModel.state.loading = false
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            if (!locationResult.locations.isNullOrEmpty()) {
                mDefaultLocation = LatLng(
                    locationResult.locations.first().latitude,
                    locationResult.locations.first().longitude
                )
                if (mMap != null) {
                    setupMapOptions()
                    stopLocationUpdates()
                }
            }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    fun getLocationFromPlacesApi(
        placeId: String
        , success: (latlng: LatLng?) -> Unit
    ) {
        var latLng: LatLng? = null
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient?.fetchPlace(request)
            ?.addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                latLng = place.latLng
                mDefaultLocation = latLng
                success(latLng)
            }?.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    success(null)
                }
            }
    }
}
