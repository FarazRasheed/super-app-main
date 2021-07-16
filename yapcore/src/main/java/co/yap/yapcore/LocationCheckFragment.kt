package co.yap.yapcore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.confirm
import co.yap.yapcore.helpers.extentions.openAppSetting
import co.yap.yapcore.helpers.permissions.PermissionEnum
import co.yap.yapcore.helpers.permissions.PermissionHelper
import co.yap.yapcore.helpers.permissions.PermissionUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import timber.log.Timber

abstract class LocationCheckFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {
    private val REQUEST_CHECK_SETTINGS = 12
    private val UPDATE_INTERVAL = 10000 // 10 sec
    private val FASTEST_INTERVAL = 5000 // 5 sec
    private val DISPLACEMENT = 10 // 10 meters
    private var permissionHelper: PermissionHelper? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    protected var latitude: Double? = null
    protected var longitude: Double? = null
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    open var mRequestingLocationUpdates = true
    private var mLocationRequest: LocationRequest? = null
    open var mShowSettingDialog = true
    private val KEY_LOCATION = 600
//    open var requestUpdateLocation = true
    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * Represents a geographical location.
     */
    protected var mCurrentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionHelper(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), KEY_LOCATION
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()

        //getLastLocation()
    }

//    override fun onResume() {
//        super.onResume()
//        checkLocationSettings()
//    }

    override fun onStart() {
        super.onStart()
        checkLocationSettings()
    }

    override fun onStop() {
        stopLocationUpdates()
        super.onStop()
    }

    private fun hasLocationPermissions() = PermissionUtils.isGranted(
        requireActivity(), PermissionEnum.ACCESS_COARSE_LOCATION,
        PermissionEnum.ACCESS_FINE_LOCATION
    )

    private fun askPermission() {
        permissionHelper?.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                // getLastLocation()
                checkLocationSettings()
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
            }

            override fun onPermissionDenied() {
            }

            override fun onPermissionDeniedBySystem() {
                confirm(
                    getString(R.string.location_permission_msg),
                    getString(R.string.location_permission)
                ) {
                    mRequestingLocationUpdates = false
                    openAppSetting(RequestCodes.REQUEST_FOR_GPS)

                }
            }
        })
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected open fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL.toLong()
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest?.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    protected fun checkLocationSettings() {
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = settingsClient.checkLocationSettings(mLocationSettingsRequest)
        task.addOnCompleteListener { t -> locationSettingsResponseOnComplete(t) }

    }

    private fun locationSettingsResponseOnComplete(t: Task<LocationSettingsResponse>) {
        try {
            val response = t.getResult(ApiException::class.java)

            startLocationUpdates()


        } catch (exception: ApiException) {
            val status = exception.statusCode
            when (status) {
                LocationSettingsStatusCodes.SUCCESS -> {

                    startLocationUpdates()

                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvable = exception as ResolvableApiException
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        if (mShowSettingDialog) {

                            resolvable.startResolutionForResult(
                                requireActivity(),
                                REQUEST_CHECK_SETTINGS
                            )
                        }
                    } catch (e: IntentSender.SendIntentException) {
//                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                    Timber.i("Location settings are inadequate, and cannot be fixed here. Dialog not created.")

            }
        }

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
//        if (!mRequestingLocationUpdates) {
//            //Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.")
//            return
//        }

        fusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener {}
    }


    private fun startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return
        }
        if (hasLocationPermissions()) {
            fusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } else {
            askPermission()
        }

    }

    private fun getLastLocation() {
        if (hasLocationPermissions()) {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { taskLocation ->
                    if (taskLocation.isSuccessful && taskLocation.result != null) {

                        mCurrentLocation = taskLocation.result
                        latitude = mCurrentLocation?.latitude
                        longitude = mCurrentLocation?.longitude
                        onLocationAvailable(mCurrentLocation)


                    }
                }
        } else {
            askPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> startLocationUpdates()
                Activity.RESULT_CANCELED -> mShowSettingDialog = false
            }// Nothing to do. startLocationupdates() gets called in onResume again.
            RequestCodes.REQUEST_FOR_GPS -> {
                if (hasLocationPermissions()) {
                    mRequestingLocationUpdates = true
                    checkLocationSettings()
                }
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(false)
        builder.setNeedBle(false)
        mLocationSettingsRequest = builder.build()
    }


    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult?.lastLocation
                latitude = mCurrentLocation?.latitude
                longitude = mCurrentLocation?.longitude

                onLocationAvailable(mCurrentLocation)

            }

            override fun onLocationAvailability(locationResults: LocationAvailability?) {
                super.onLocationAvailability(locationResults)
            }
        }
    }

    abstract fun onLocationAvailable(location: Location?)


}