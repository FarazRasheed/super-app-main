package co.yap.modules.location.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.location.interfaces.ILocation
import co.yap.modules.location.viewmodels.LocationViewModel
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants.ADDRESS
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator

class LocationSelectionActivity : BaseBindingActivity<ILocation.ViewModel>(), ILocation.View,
    INavigator, IFragmentHolder {

    companion object {
        private const val HEADING = "heading"
        private const val SUB_HEADING = "subHeading"
        private const val IS_ON_BOARDING = "isOnBoarding"
        fun newIntent(
            context: Context,
            address: Address,
            headingTitle: String = "",
            subHeadingTitle: String = "",
            onBoarding: Boolean = false
        ): Intent {
            val intent = Intent(context, LocationSelectionActivity::class.java)
            intent.putExtra(HEADING, headingTitle)
            intent.putExtra(SUB_HEADING, subHeadingTitle)
            intent.putExtra(ADDRESS, address)
            intent.putExtra(IS_ON_BOARDING, onBoarding)
            return intent
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_location

    override val viewModel: ILocation.ViewModel
        get() = ViewModelProviders.of(this).get(LocationViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settDataFromIntent()
    }

    private fun settDataFromIntent() {
        //address
        if (intent != null && intent.hasExtra(ADDRESS)) {
            val data: Address? = intent.getParcelableExtra(ADDRESS)
            data?.let {
                viewModel.address = it
            }
        }
        //flag is from onboarding
        viewModel.isOnBoarding =
            (intent?.getValue(IS_ON_BOARDING, ExtraType.BOOLEAN.name) as? Boolean) ?: false
        //heading
        val heading = intent?.getValue(HEADING, ExtraType.STRING.name) as? String
        heading?.let {
            viewModel.defaultHeading = it
            viewModel.heading = it
        }
        //sub heeading
        val subHeading = intent?.getValue(SUB_HEADING, ExtraType.STRING.name) as? String
        subHeading?.let { viewModel.subHeading = subHeading }
    }

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@LocationSelectionActivity,
            R.id.location_nav_host_fragment
        )

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.location_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }


}