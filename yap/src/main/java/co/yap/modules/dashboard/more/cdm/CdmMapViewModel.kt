package co.yap.modules.dashboard.more.cdm

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.responsedtos.AtmCdmData
import co.yap.networking.models.RetroApiResponse
import co.yap.widgets.State
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.extentions.getScreenHeight
import co.yap.yapcore.helpers.extentions.getScreenWidth
import co.yap.yapcore.helpers.extentions.isValidLatLng
import co.yap.yapcore.helpers.extentions.parseToDouble
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class CdmMapViewModel(application: Application) : BaseViewModel<ICdmMap.State>(application),
    ICdmMap.ViewModel {
    override val state: CdmMapState = CdmMapState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    private val cardsRepository: CardsRepository = CardsRepository
    private var moveCameraToCurrentLocation = true
    override var currentLocation: Location? = null
        set(value) {
            field = value
            mMap?.run {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = false
                if (moveCameraToCurrentLocation)
                    animateMapToLocation(LatLng(value?.latitude!!, value.longitude))

            }

        }
    override var mMap: GoogleMap? = null
        set(value) {
            field = value
            mMap?.setOnMapLoadedCallback {
                mMap?.run {
                    isIndoorEnabled = false
                    isBuildingsEnabled = true
                    isTrafficEnabled = false
                    uiSettings.isZoomControlsEnabled = false
                    uiSettings.isCompassEnabled = true
                    uiSettings.isMapToolbarEnabled = true
                    uiSettings.isMyLocationButtonEnabled = false
                    uiSettings.setAllGesturesEnabled(true)
//                    mMap?.setOnMyLocationButtonClickListener {
//                        false
//                    }
                }

            }
        }

    override fun onCreate() {
        super.onCreate()
        getCardsAtmCdm(state.locationType?.value)
    }

    override fun handleClickEvent(id: Int) {
        clickEvent.postValue(id)
    }

    private fun addMarker(latlan: LatLng, item: AtmCdmData?, index: Int): Marker? {
        val option = MarkerOptions()
        option.icon
        option.position(latlan)
        option.title(item?.name)
        val marker = mMap?.addMarker(option)
        marker?.tag = item

        mMap?.setOnMarkerClickListener { p0 ->
            p0.showInfoWindow()
            state.atmCdmData = p0.tag as AtmCdmData
            true
        }
        if (index == 0) {
            marker?.showInfoWindow()
        }
        return marker
    }

    private fun addMarker(latlan: LatLng, title: String, resId: Int) {
        val option = MarkerOptions()
        option.icon
        option.position(LatLng(latlan.latitude, latlan.longitude))
            .icon(bitmapDescriptorFromVector(getApplication(), resId))
        mMap?.addMarker(option)
    }

    override fun getCardsAtmCdm(type: String?) {
        launch {
            state.stateLiveData.postValue(State.loading(""))
            //state.loading = true
            when (val response = cardsRepository.getCardsAtmCdm()) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let { it ->
                        var data = it
                        if (it.isNotEmpty()) {
                            // filter data here based on type
                            if (!state.locationType?.value.isNullOrBlank()) data = it.filter {
                                it?.type == state.locationType?.value
                            }

                            if (data.isNotEmpty()) {
                                moveCameraToCurrentLocation = false
                                state.stateLiveData.postValue(State.success(""))
                                val latLans = ArrayList<LatLng>()
                                var validLatLng: LatLng = LatLng(0.0, 0.0)
                                for (i in data.indices) {
                                    val latLan = LatLng(
                                        data[i]?.latitude?.parseToDouble()!!,
                                        data[i]?.longitude?.parseToDouble()!!
                                    )
                                    if (i == 0) {
                                        state.atmCdmData = data[i]
                                        //animateMapToLocation(latLan)
                                    }
                                    if (isValidLatLng(latLan.latitude, latLan.longitude)) {
                                        addMarker(latLan, data[i], i)
                                        latLans.add(latLan)
                                        if (validLatLng.latitude == 0.0 && validLatLng.longitude == 0.0) {
                                            moveCameraToCurrentLocation = false
                                            validLatLng = latLan
                                            animateMapToLocation(validLatLng)
                                        }
                                    }


                                }
                            }
                            //animateMapToLocation(latLans)
                        } else {
                            state.stateLiveData.postValue(State.empty(""))
                        }
                    }

                }
                is RetroApiResponse.Error ->
                    state.stateLiveData.postValue(State.error(response.error.message))
            }
        }

    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.apply {
            setBounds(
                0,
                0,
                intrinsicWidth,
                intrinsicHeight
            )

            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        return null!!
    }

    private fun animateMapToLocation(latlan: LatLng?) {

        mMap?.run {
            val location = CameraUpdateFactory.newLatLngZoom(
                latlan, 9f
            )
            animateCamera(location)
        }
    }

    private fun animateMapToLocation(locations: ArrayList<LatLng>) {

        mMap?.run {
            val builder = LatLngBounds.Builder()
            locations.forEach { it ->
                builder.include(it)
            }
            val bounds = builder.build()
            getScreenWidth()
            val width = getScreenWidth()
            val height = getScreenHeight()
            val padding = (width * 0.10).toInt()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            animateCamera(cu)

        }

    }
}
