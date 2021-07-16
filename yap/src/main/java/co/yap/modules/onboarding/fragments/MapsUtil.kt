package co.yap.modules.onboarding.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapsUtil : OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {
    internal lateinit var mLastLocation: Location
    internal lateinit var start: Location
    internal var mCurrLocationMarker: Marker? = null
    internal lateinit var addresses: List<Address>
    var address: String? = null
    internal lateinit var postalCode: String
    internal lateinit var geocoder: Geocoder
    internal lateinit var mMap: GoogleMap
    internal lateinit var latLng: LatLng
    internal lateinit var mLocationRequest: LocationRequest
    private lateinit var context: Context
    internal var mGoogleApiClient: GoogleApiClient? = null

    internal lateinit var supportMapFragment: SupportMapFragment
    internal lateinit var location: Location
    internal lateinit var locationManager: LocationManager

    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()

    constructor() {

    }

    constructor(context: Context, supportMapFragment: SupportMapFragment) {
        this.context = context
        this.supportMapFragment = supportMapFragment
        supportMapFragment.getMapAsync(this)
         buildGoogleApiClient()
         locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(context, Locale.getDefault())
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
//        if (ca.isConnectingToInternet()) {
            mGoogleApiClient = GoogleApiClient.Builder(Objects.requireNonNull(context))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
            mGoogleApiClient!!.connect()
//        } else {
//            ca.showToast(context.resources.getString(R.string.err_network_connection))
//        }
    }


    override fun onConnected(bundle: Bundle?) {
        //checkPermissions();
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                Objects.requireNonNull(context),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getMyLocation()
            mMap.isMyLocationEnabled = true

        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {
        locationChange(location)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        setOnMyLocationButtonClickListener()
    }

    fun locationChange(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        start = location
        latLng = LatLng(location.latitude, location.longitude)
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            address = addresses[0].getAddressLine(0)
            if (null != address) {
                latitude = location.latitude
                longitude = location.longitude
//                EventBus.getDefault().post(address)
            }
//            sharedPreferencesManager.setString(SharedPreferencesManager.KEY_ADDRESS, address)
            postalCode = addresses[0].postalCode
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            markerOptions.title(address)
            mCurrLocationMarker = mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14f))

        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    fun setMarkerSearchLocation(data: Intent) {
        val place = PlaceAutocomplete.getPlace(Objects.requireNonNull(context), data)
        try {
            addresses = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
            address = addresses[0].getAddressLine(0)
//            sharedPreferencesManager.setString(SharedPreferencesManager.KEY_ADDRESS, address)
            mMap.clear()
            val markerOptions = MarkerOptions()
            markerOptions.position(place.latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            markerOptions.title(address)
            mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14f))

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ee: NullPointerException) {
            ee.printStackTrace()
        }


    }


    fun getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient!!.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(
                    Objects.requireNonNull(context),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest)
                    builder.setAlwaysShow(true)
                    LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                    val result = LocationServices.SettingsApi
                        .checkLocationSettings(mGoogleApiClient, builder.build())
                    result.setResultCallback { result1 ->
                        val status = result1.status
                        when (status.statusCode) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                val permissionLocation1 = ContextCompat
                                    .checkSelfPermission(
                                        context,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                if (permissionLocation1 == PackageManager.PERMISSION_GRANTED) {
                                    location = LocationServices.FusedLocationApi
                                        .getLastLocation(mGoogleApiClient)
                                    mMap.isMyLocationEnabled = true
                                    setOnMyLocationButtonClickListener()
                                }
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(
                                        context as Activity,
                                        200
                                    )
                                } catch (e: IntentSender.SendIntentException) {
                                    // Ignore the error.
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            }
                        }// Location settings are not satisfied.
                        // However, we have no way
                        // to fix the
                        // settings so we won'retrofit show the dialog.
                        // finish();
                    }
                }
            }
        }
    }


    fun setMarker(latitude: Double, longitude: Double) {
        try {
            val latLng = LatLng(latitude, longitude)
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            address = addresses[0].getAddressLine(0)
            if (null != address) {
                this.latitude = latitude
                this.longitude = longitude
//                EventBus.getDefault().post(address)
            }
//            sharedPreferencesManager.setString(SharedPreferencesManager.KEY_ADDRESS, address)
            postalCode = addresses[0].postalCode
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            markerOptions.title(address)
            mCurrLocationMarker = mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14f))
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            getMyLocation()
        }

    }

    fun setOnMyLocationButtonClickListener() {
        mMap.setOnMyLocationButtonClickListener {

            try {
                val latLng = LatLng(location.latitude, location.longitude)
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                address = addresses[0].getAddressLine(0)
                if (null != address) {
                    latitude = location.latitude
                    longitude = location.longitude
//                    EventBus.getDefault().post(address)
                }
//                sharedPreferencesManager.setString(SharedPreferencesManager.KEY_ADDRESS, address)
                postalCode = addresses[0].postalCode
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                markerOptions.title(address)
                mCurrLocationMarker = mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14f))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*catch (NullPointerException e) {
            e.printStackTrace();
            getMyLocation();
        } */

            false
        }
    }


}
