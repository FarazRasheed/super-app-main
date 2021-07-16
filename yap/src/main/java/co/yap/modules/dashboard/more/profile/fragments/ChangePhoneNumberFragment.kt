package co.yap.modules.dashboard.more.profile.fragments

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.main.fragments.MoreBaseFragment
import co.yap.modules.dashboard.more.profile.intefaces.IChangePhoneNumber
import co.yap.modules.dashboard.more.profile.viewmodels.ChangePhoneNumberViewModel
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.OtpDataModel
import co.yap.translation.Strings
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.extentions.startFragmentForResult
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_change_phone_number.*

class ChangePhoneNumberFragment : MoreBaseFragment<IChangePhoneNumber.ViewModel>(),
    IChangePhoneNumber.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_change_phone_number
    override val viewModel: IChangePhoneNumber.ViewModel
        get() = ViewModelProviders.of(this).get(ChangePhoneNumberViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.changePhoneNumberSuccessEvent.observe(this, Observer {
            SessionManager.user?.currentCustomer?.mobileNo = viewModel.state.mobile.replace(" ", "")
            SessionManager.user?.currentCustomer?.countryCode = viewModel.state.countryCode
            SharedPreferenceManager(requireContext()).saveUserNameWithEncryption( viewModel.state.mobile.replace(" ", ""))
            val action =
                ChangePhoneNumberFragmentDirections.actionChangePhoneNumberFragmentToSuccessFragment(
                    getString(Strings.screen_phone_number_success_display_text_sub_heading),
                    SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(requireContext()).toString()
                )
            findNavController().navigate(action)
        })
    }

    private fun setObservers() {
        viewModel.isPhoneNumberValid.observe(this, Observer {
            if (it) startOtpFragment()

        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.countryCode = ccpSelector.getDefaultCountryCode()
        viewModel.state.etMobileNumber = etNewMobileNumber
    }

    private fun startOtpFragment() {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    OTPActions.CHANGE_MOBILE_NO.name,
                    "+${viewModel.state.countryCode + " " + viewModel.state.mobile}"
                )
            ),
            showToolBar = true
        ) { resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                viewModel.changePhoneNumber()
            }
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        viewModel.isPhoneNumberValid.removeObservers(this)
        super.onDestroy()
    }
}