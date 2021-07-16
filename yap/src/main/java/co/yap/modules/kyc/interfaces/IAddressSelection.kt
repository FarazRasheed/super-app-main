package co.yap.modules.kyc.interfaces

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Location
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

interface IAddressSelection {

    interface View : IBase.View<ViewModel> {
        fun onSuccessEvent()

    }

    interface ViewModel : IBase.ViewModel<State> {
        var checkGps: Boolean
        var mapFragment: SupportMapFragment?
        var mLastKnownLocation: Location

        var mapDetailViewActivity: Activity
        val clickEvent: SingleClickEvent
        val onSuccess: MutableLiveData<Int>
        val MARKER_CLICK_ID: Int
        val GPS_CLICK_EEVENT: Int
        val UPDATE_ADDRESS_EEVENT: Int
        val ON_UPDATE_ADDRESS_EVENT: Int
        val ON_ADD_NEW_ADDRESS_EVENT: Int
        fun handlePressOnNext(id: Int)
        fun handlePressOnSelectLocation(id: Int)
        fun handlePressOnCardSelectLocation(id: Int)
        fun handlePressOnCloseMap(id: Int)
        fun initMap()
        fun onMapInit(p0: GoogleMap?)
        var selectedLocationLatitude: Double
        var selectedLocationLongitude: Double

        fun getDeviceLocation(activity: Activity)
        fun getDefaultLocationMap(activity: Activity)
        fun onLocatioenSelected()
        fun toggleMarkerVisibility()
        fun setUpCardFields()
        fun requestUpdateAddress(updateAddressRequest: Address)
        var updateAddressRequest: Address
        var mDefaultLocation : LatLng
    }

    interface State : IBase.State {
        var headingTitle: String
        var subHeadingTitle: String
        var addressField: String
        var landmarkField: String
        var locationBtnText: String
        var nextActionBtnText: String
        var valid: Boolean
        var checked: Boolean
        var isFromPersonalDetailView: Boolean
        var isFromPhysicalCardsLayout: Boolean

        //map detail

        var placePhoto: Bitmap?
        var placeTitle: String
        var placeSubTitle: String
        var closeCard: Boolean
        var cardView: Boolean
        var confirmLocationButton: Boolean
        var isMapOnScreen: Boolean
        var googleMap: GoogleMap?
        var errorVisibility: Int
        var checkBoxLayoutVisibility: Int
        var errorChecked: Boolean
        var setDrawable: Drawable?
        var addressTitlesColor: Int
        var landMarkTitleColor: Int
        var onDrawableClick: Boolean
    }
}