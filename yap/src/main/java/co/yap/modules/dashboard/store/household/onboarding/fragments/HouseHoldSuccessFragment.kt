package co.yap.modules.dashboard.store.household.onboarding.fragments

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.store.household.onboarding.HouseHoldOnboardingActivity
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldSuccess
import co.yap.modules.dashboard.store.household.onboarding.viewmodels.HouseHoldSuccessViewModel
import co.yap.translation.Strings
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.managers.SessionManager

class HouseHoldSuccessFragment : BaseOnBoardingFragment<IHouseHoldSuccess.ViewModel>(),
    IHouseHoldSuccess.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_house_hold_success

    override val viewModel: HouseHoldSuccessViewModel
        get() = ViewModelProviders.of(this).get(HouseHoldSuccessViewModel::class.java)

    override fun onResume() {
        super.onResume()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnGoToHouseHold -> {
                    findNavController().navigate(R.id.action_houseHoldSuccessFragment_to_yapDashboardActivity)
                    if (activity is HouseHoldOnboardingActivity) {
                        (activity as HouseHoldOnboardingActivity).setIntentResult(true)
                    }
                }

                R.id.btnShare -> {
                    shareInfo()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun shareInfo() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getBody())
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

    private fun getBody(): String {
        return getString(Strings.screen_yap_house_hold_confirm_payment_share_text).format(
            viewModel.parentViewModel?.firstName,
            SessionManager.user?.currentCustomer?.firstName,
            viewModel.parentViewModel?.userMobileNo,
            viewModel.parentViewModel?.tempPasscode,
            Constants.URL_SHARE_APP_STORE,
            Constants.URL_SHARE_PLAY_STORE
        )
    }

    override fun onBackPressed(): Boolean = true
}