package co.yap.modules.dashboard.more.changepasscode.fragments

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.OtpDataModel
import co.yap.modules.passcode.IPassCode
import co.yap.modules.passcode.PassCodeViewModel
import co.yap.translation.Strings
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.databinding.FragmentPassCodeBinding
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.startFragmentForResult

class UpdateConfirmPasscodeFragment : ChangePasscodeBaseFragment<IPassCode.ViewModel>(),
    IPassCode.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_pass_code
    override val viewModel: IPassCode.ViewModel
        get() = ViewModelProviders.of(this).get(PassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.setTitles(
            title = getString(Strings.screen_confirm_passcode_display_text_heading),
            buttonTitle = getString(Strings.screen_current_card_pin_display_button_next)
        )
        viewModel.token = parentActivity.passCodeData.token
        viewModel.state.forgotTextVisibility = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getBinding().dialer.upDatedDialerPad(viewModel.state.passCode)
        getBinding().dialer.hideFingerprintView()
    }

    fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    if (viewModel.state.passCode == parentActivity.passCodeData.newPassCode) {
                        viewModel.updatePassCodeRequest {
                            moveToSuccessScreen()
                        }
                    } else
                        getBinding().dialer.startAnimation()
                }
                R.id.tvForgotPasscode -> {
                    viewModel.createForgotPassCodeOtp {username->
                        startOtpFragment(username)
                    }
                }
            }
        })
    }

    private fun moveToSuccessScreen() {
        val sharedPreferenceManager = SharedPreferenceManager(requireContext())
        sharedPreferenceManager.savePassCodeWithEncryption(viewModel.state.passCode)
        findNavController().navigate(R.id.action_updateConfirmPasscodeFragment_to_successFragment)
    }

    private fun startOtpFragment(name: String) {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    otpAction = OTPActions.FORGOT_PASS_CODE.name,
                    mobileNumber = viewModel.mobileNumber,
                    username = name,
                    emailOtp = !Utils.isUsernameNumeric(name)
                )
            ),
            showToolBar = true
        ) { resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                val token =
                    data?.getValue(
                        "token",
                        ExtraType.STRING.name
                    ) as? String

                viewModel.mobileNumber = (data?.getValue(
                    "mobile",
                    ExtraType.STRING.name
                ) as? String) ?: ""

                token?.let {
                    viewModel.token = it
                    navigateToForgotPassCodeFlow()
                }
            }
        }
    }

    private fun navigateToForgotPassCodeFlow() {
        if (viewModel.isUserLoggedIn()) {
                val action =
                    UpdateConfirmPasscodeFragmentDirections.actionUpdateConfirmPasscodeFragmentToForgotPasscodeNavigation(
                        viewModel.mobileNumber,
                        viewModel.token,
                        Constants.FORGOT_PASSCODE_FROM_CHANGE_PASSCODE
                    )
            navigate(action,screenType = FeatureSet.FORGOT_PASSCODE)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun getBinding(): FragmentPassCodeBinding {
        return (viewDataBinding as FragmentPassCodeBinding)
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                activity?.onBackPressed()
            }
        }
    }
}