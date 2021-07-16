package co.yap.modules.location.fragments

import android.Manifest
import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.yap.modules.location.CitiesListBottomSheet
import co.yap.modules.location.helper.MapSupportFragment
import co.yap.modules.location.interfaces.ILocationSelection
import co.yap.modules.webview.WebViewFragment
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.customers.responsedtos.City
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.ADDRESS
import co.yap.yapcore.constants.Constants.ADDRESS_SUCCESS
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.databinding.LocationSelectionFragmentBinding
import co.yap.yapcore.enums.AccountStatus
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.hideKeyboard
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.helpers.permissions.PermissionHelper
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.leanplum.trackEventInFragments
import co.yap.yapcore.managers.SessionManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ezaka.customer.app.utils.hideKeyboard
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.layout_google_maps.*
import kotlinx.android.synthetic.main.location_selection_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LocationSelectionFragment : MapSupportFragment(), ILocationSelection.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.parentViewModel?.isOnBoarding == true) {
            when (SessionManager.user?.notificationStatuses) {
                AccountStatus.MEETING_SCHEDULED.name, AccountStatus.BIRTH_INFO_COLLECTED.name -> {
                    skipLocationSelectionFragment()
                }
                else -> setObservers()
            }
        } else {
            setObservers()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.parentViewModel?.isOnBoarding == true) {
            when (SessionManager.user?.notificationStatuses) {
                AccountStatus.MEETING_SCHEDULED.name, AccountStatus.BIRTH_INFO_COLLECTED.name -> {
                }
                else -> {
                    checkPermission()
                    setHeadings()
                    setAddress()
                    addListeners()
                }
            }
        } else {
            checkPermission()
            setHeadings()
            setAddress()
            addListeners()
        }

    }

    private fun addListeners() {
        flTitle.setOnTouchListener { _, _ -> true }
        transparentImage.setOnTouchListener { _, _ -> !((viewModel.isMapExpanded.value) ?: false) }
    }

    private fun checkGPS() {
        if (!isGPSEnabled()) {
            Utils.confirmationDialog(
                requireContext(),
                "Location",
                "Your GPS seems to be disabled, do you want to enable it?",
                "Yes",
                "No",
                object : OnItemClickListener {
                    override fun onItemClick(view: View, data: Any, pos: Int) {
                        if (data is Boolean) {
                            if (data) {
                                startActivityForResult(
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                    RequestCodes.REQUEST_FOR_GPS
                                )
                            }
                        }
                    }
                })
        } else {
            initMapFragment()
        }
    }

    private fun setAddress() {
        viewModel.address = viewModel.parentViewModel?.address
        viewModel.state.addressTitle.set(viewModel.address?.address1)
        viewModel.state.addressSubtitle.set(viewModel.address?.address2)
        populateCardState(viewModel.address, true)
        getCurrentLocation()

    }

    private fun setHeadings() {
        viewModel.defaultHeading = viewModel.parentViewModel?.defaultHeading ?: ""
        viewModel.state.headingTitle.set(viewModel.parentViewModel?.heading)
        viewModel.state.subHeadingTitle.set(viewModel.parentViewModel?.subHeading)
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.selectedPlaceId.observe(this, autoCompleteLocationListener)
        viewModel.state.isTermsChecked.addOnPropertyChangedCallback(stateObserver)
        viewModel.state.addressSubtitle.addOnPropertyChangedCallback(stateObserver)
        viewModel.state.addressTitle.addOnPropertyChangedCallback(stateObserver)
        viewModel.state.city.addOnPropertyChangedCallback(stateObserver)
        viewModel.isMapExpanded.observe(this, Observer {
            viewModel.state.toolbarVisibility = !it
            if (it) {
                activity?.hideKeyboard()
                rlCollapsedMapSection.visibility = View.GONE
                ivClose.visibility = View.VISIBLE
            } else {
                rlCollapsedMapSection.visibility = View.VISIBLE
                ivClose.visibility = View.GONE
            }
        })
    }

    private fun initMapFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.let {
            it.getMapAsync { googleMap ->
                googleMap?.let { map ->
                    onMapReady(map)
                }
            }
        }
    }

    private val stateObserver = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (viewModel.state.isTermsChecked.get() == true) {
                viewModel.termsCheckedTime.value =
                    SimpleDateFormat(
                        DateUtils.LEAN_PLUM_EVENT_FORMAT,
                        Locale.US
                    ).format(Calendar.getInstance().time)
            }
            viewModel.state.valid.set(
                !viewModel.state.addressTitle.get().isNullOrBlank()
                        && !viewModel.state.addressSubtitle.get().isNullOrBlank()
                        && viewModel.state.city.get() != "Select"
            )
        }
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.nextButton -> {
                if (viewModel.parentViewModel?.isOnBoarding == true && viewModel.isValidAddress())
                    viewModel.requestOrderCard(viewModel.getUserAddress()) {
                        viewModel.address?.city?.let { city ->
                            trackEventInFragments(
                                SessionManager.user,
                                city = city
                            )
                        }
                        findNavController().navigate(R.id.action_locationSelectionFragment_to_POBSelectionFragment)
                    }
                else
                    setIntentAction(true)
            }

            R.id.btnLocation -> {
                onMapClickAction()
                removeAutoCompleteFocus()
            }

            R.id.ivClose -> {
                if (viewModel.state.isShowLocationCard.get() == true)
                    slideDownCardAnimation()
                else {
                    collapseMap()
                }
            }

            R.id.btnConfirm -> {
                startAnimateLocationCard()
            }
            R.id.tvTermsAndConditions -> {
                startFragment(
                    fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                        Constants.PAGE_URL to Constants.URL_TERMS_CONDITION
                    ), showToolBar = false
                )
            }
            R.id.etAddressField -> {

                if (getBinding().etAddressField.length() == 0 && !viewModel.hasSeletedLocation) {
                    onMapClickAction()
                }
            }

            R.id.rlCollapsedMapSection -> {
                onMapClickAction()
            }

            R.id.layoutCitiesBottomSheet -> {
                setupCitiesList(viewModel.cities.value)
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                setIntentAction(false)
            }
        }
    }

    private fun setupCitiesList(citiesList: ArrayList<City>?) {
        citiesList?.let { cities ->
            this.childFragmentManager.let {
                val citiesListBottomSheet = CitiesListBottomSheet(object :
                    OnItemClickListener {
                    override fun onItemClick(view: View, data: Any, pos: Int) {
                        (data as? CitiesListBottomSheet)?.dismiss()
                        viewModel.state.city.set(cities[pos].name)
                    }
                }, cities)
                citiesListBottomSheet.show(it, "")
            }
        } ?: viewModel.showMessage("No city found")

    }

    private fun onMapClickAction() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplicitPermissionDialog()
            } else {
                checkPermission()
            }
        } else {
            checkGPS()
            if (isGPSEnabled()) {
                expandMap()
            }
        }
    }

    private fun expandMap() {
        viewModel.isMapExpanded.value = true
        YoYo.with(Techniques.FadeOut)
            .duration(200)
            .playOn(btnLocation)

        YoYo.with(Techniques.SlideOutUp)
            .duration(600)
            .playOn(flTitle)

        YoYo.with(Techniques.SlideOutDown)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (!isFromPlacesApi) {
                        mDefaultLocation?.let {
                            loadAysnMapInfo(it)
                            animateCameraToLocation(it)
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {

                }
            })
            .duration(600)
            .playOn(flAddressDetail)
    }

    private fun collapseMap() {
        viewModel.isMapExpanded.value = false

        YoYo.with(Techniques.FadeIn)
            .duration(400)
            .playOn(btnLocation)

        YoYo.with(Techniques.SlideInDown)
            .duration(400)
            .playOn(flTitle)

        YoYo.with(Techniques.SlideInUp)
            .duration(400)
            .playOn(flAddressDetail)
        addAutoCompleteFocus()

    }

    private fun startAnimateLocationCard() {
        YoYo.with(Techniques.SlideOutDown)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    viewModel.state.isShowLocationCard.set(false)
                    collapseMap()
                    viewModel.onLocationSelected()
                    getBinding().etAddressField.setSelection(getBinding().etAddressField.length())
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(300)
            .playOn(cvLocationCard)
    }

    private fun slideDownCardAnimation() {
        YoYo.with(Techniques.SlideOutDown)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    viewModel.state.isShowLocationCard.set(false)
                    collapseMap()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(300)
            .playOn(cvLocationCard)
    }

    private fun setIntentAction(isUpdated: Boolean) {
        if (viewModel.isValidAddress()) {
            val intent = Intent()
            intent.putExtra(ADDRESS, viewModel.getUserAddress())
            intent.putExtra(ADDRESS_SUCCESS, isUpdated)
            intent.putExtra(Constants.PLACES_PHOTO_ID, viewModel.selectedPlaceId.value.toString())
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun checkPermission() {
        permissionHelper = PermissionHelper(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 100
        )
        permissionHelper?.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                checkGPS()
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {}

            override fun onPermissionDenied() {}

            override fun onPermissionDeniedBySystem() {
                showExplicitPermissionDialog()
            }
        })
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showExplicitPermissionDialog() {
        Utils.confirmationDialog(
            requireContext(), "Location", "Need permission for location, do you want to enable it?",
            "Yes", "No", object : OnItemClickListener {
                override fun onItemClick(view: View, data: Any, pos: Int) {
                    if (data is Boolean) {
                        if (data) {
                            permissionHelper?.openAppDetailsActivity()
                        }
                    }
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHelper != null) {
            permissionHelper?.onRequestPermissionsResult(
                requestCode,
                permissions as Array<String>,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_FOR_GPS) {
            checkGPS()
        }
    }

    override fun onBackPressed(): Boolean {
        return if (viewModel.isMapExpanded.value == true) {
            slideDownCardAnimation()
            true
        } else
            false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.isMapExpanded.removeObservers(this)
        viewModel.state.isTermsChecked.removeOnPropertyChangedCallback(stateObserver)
        viewModel.state.addressSubtitle.removeOnPropertyChangedCallback(stateObserver)
        viewModel.state.addressTitle.removeOnPropertyChangedCallback(stateObserver)
        viewModel.state.city.removeOnPropertyChangedCallback(stateObserver)
    }

    private fun skipLocationSelectionFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.locationSelectionFragment, true) // starting destination skiped
            .build()

        findNavController().navigate(
            R.id.action_locationSelectionFragment_to_POBSelectionFragment,
            null,
            navOptions
        )
    }


    private fun removeAutoCompleteFocus() {
        getBinding().etAddressField.isFocusable = false
        getBinding().etAddressField.isFocusableInTouchMode = false
        getBinding().etAddressField.isFocusable = false
        getBinding().etAddressField.isFocusableInTouchMode = false
        getBinding().etAddressField.hideKeyboard()
    }

    private fun addAutoCompleteFocus() {
        viewModel.viewModelScope.launch {
            delay(1000)
            getBinding().etAddressField.isFocusable = true
            getBinding().etAddressField.isFocusableInTouchMode = true
            getBinding().etAddressField.isFocusable = true
            getBinding().etAddressField.isFocusableInTouchMode = true
        }

    }

    private val autoCompleteLocationListener = Observer<String> {
        when {
            !it.isNullOrEmpty() -> {
                getLocationFromPlacesApi(it) { latlng ->
                    latlng?.let { it1 ->
                        isFromPlacesApi = true
                        viewModel.state.isShowLocationCard.set(true)
                        removeAutoCompleteFocus()
                        animateCameraToLocation(it1)
                        val address = getAddressFromPlacesApi(latlng)
                        populateCardState(address, false)
                        viewModel.onLocationSelected()
                    }
                }
            }
        }
    }

    private fun getAddressFromPlacesApi(latLng: LatLng): Address {
        return Address(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            address1 = viewModel.state.placeTitle.get(),
            address2 = viewModel.state.placeSubTitle.get(),
            city = viewModel.state.city.get(),
            country = "United Arab Emirates",
            nearestLandMark = viewModel.state.placeTitle.get()
        )
    }

    override fun getBinding(): LocationSelectionFragmentBinding {
        return viewDataBinding as LocationSelectionFragmentBinding
    }
}