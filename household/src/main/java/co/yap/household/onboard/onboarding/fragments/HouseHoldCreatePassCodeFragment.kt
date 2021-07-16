package co.yap.household.onboard.onboarding.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.household.BR
import co.yap.household.R
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldCreatePassCode
import co.yap.household.onboard.onboarding.viewmodels.HouseHoldCreatePassCodeViewModel
import co.yap.modules.webview.WebViewFragment
import co.yap.widgets.NumberKeyboardListener
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.startFragment
import kotlinx.android.synthetic.main.fragment_house_hold_create_passcode.*

class HouseHoldCreatePassCodeFragment :
    OnboardingChildFragment<IHouseHoldCreatePassCode.ViewModel>(), IHouseHoldCreatePassCode.View,
    NumberKeyboardListener {

    override fun getLayoutId(): Int = R.layout.fragment_house_hold_create_passcode
    override fun getBindingVariable() = BR.createPasscodeViewModel

    override val viewModel: IHouseHoldCreatePassCode.ViewModel
        get() = ViewModelProviders.of(this).get(HouseHoldCreatePassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialer.setNumberKeyboardListener(this)
        dialer.hideFingerprintView()
    }

    override fun setObservers() {
        viewModel.clickEvent?.observe(this, Observer {
            when (it) {
                R.id.btnCreatePasscode -> {
                    viewModel.createPassCodeRequest()
                }
                R.id.tvTermsAndConditions -> {
                    startFragment(
                        fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                            Constants.PAGE_URL to Constants.URL_TERMS_CONDITION
                        ),showToolBar = false
                    )
                    //Utils.openWebPage(Constants.URL_TERMS_CONDITION, "", activity)
                }
            }
        })
        viewModel.onPasscodeSuccess.observe(this, Observer {
            if (it) findNavController().navigate(R.id.to_emailHouseHoldFragment)
        })
    }

    override fun onBackPressed(): Boolean = true
    override fun onNumberClicked(number: Int, text: String) {
        viewModel.state.passcode = dialer.getText()
        viewModel.state.dialerError = ""
    }

    override fun onLeftButtonClicked() {
    }

    override fun onRightButtonClicked() {
        viewModel.state.dialerError = ""
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent?.removeObservers(this)
        viewModel.onPasscodeSuccess.removeObservers(this)
    }
}