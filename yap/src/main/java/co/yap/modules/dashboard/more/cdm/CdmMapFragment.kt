package co.yap.modules.dashboard.more.cdm

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.widgets.MultiStateView
import co.yap.widgets.State
import co.yap.widgets.Status
import co.yap.yapcore.LocationCheckFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.openGoogleMapDirection
import co.yap.yapcore.helpers.extentions.parseToDouble
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_cdm_map.*

class CdmMapFragment : LocationCheckFragment<ICdmMap.ViewModel>(), ICdmMap.View,
    OnMapReadyCallback {
    private var mMap: GoogleMap? = null

    override fun getBindingVariable() = BR.cdmMapViewModel

    override fun getLayoutId() = R.layout.fragment_cdm_map

    override val viewModel: ICdmMap.ViewModel
        get() = ViewModelProviders.of(this).get(CdmMapViewModel::class.java)


    override fun onLocationAvailable(location: Location?) {
        viewModel.currentLocation = location
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            viewModel.state.locationType?.value = it.getString(Constants.LOCATION_TYPE, "")
        }
        initMap()
        viewModel.state.stateLiveData.observe(this, Observer { handleState(it) })
        viewModel.clickEvent.observe(this, Observer { handleClickEvent(it) })
        multiStateView.setOnReloadListener(object : MultiStateView.OnReloadListener {
            override fun onReload(view: View) {
                viewModel.getCardsAtmCdm(viewModel.state.locationType?.value)
            }
        })

    }

    private fun handleClickEvent(id: Int) {
        when (id) {
            R.id.btnDirection -> openGoogleMapDirection(
                LatLng(
                    viewModel.state.atmCdmData?.latitude.parseToDouble(),
                    viewModel.state.atmCdmData?.longitude.parseToDouble()
                )
            )
            R.id.ivBackBtn -> activity?.finish()

        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.state.stateLiveData.removeObservers(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.mMap = googleMap
        mMap = googleMap
    }

    private fun handleState(state: State?) {
        when (state?.status) {
            Status.LOADING -> multiStateView?.viewState =
                MultiStateView.ViewState.LOADING
            Status.EMPTY -> {
                multiStateView?.viewState = MultiStateView.ViewState.EMPTY
            }
            Status.ERROR -> {
                multiStateView?.viewState = MultiStateView.ViewState.ERROR
            }
            else ->
                multiStateView?.viewState = MultiStateView.ViewState.CONTENT

        }
    }
}
