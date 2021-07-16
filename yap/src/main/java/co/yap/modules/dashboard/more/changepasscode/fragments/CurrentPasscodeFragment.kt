package co.yap.modules.dashboard.more.changepasscode.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

class CurrentPasscodeFragment : ChangePasscodeBaseFragment<IPassCode.ViewModel>(), IPassCode.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_pass_code

    override val viewModel: IPassCode.ViewModel
        get() = ViewModelProviders.of(this).get(PassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.setTitles(
            title = getString(Strings.screen_current_passcode_display_text_heading),
            buttonTitle = getString(Strings.screen_current_card_pin_display_button_next)
        )
        parentActivity.viewModel.state.toolbarVisibility.set(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().dialer.upDatedDialerPad(viewModel.state.passCode)
        getBinding().dialer.hideFingerprintView()
        viewModel.state.forgotTextVisibility = true
    }

    fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    viewModel.validatePassCode { isValidPassCode ->
                        if (isValidPassCode) {
                            parentActivity.passCodeData.token = viewModel.token
                            navigate(
                                R.id.action_currentPasscodeFragment_to_updateNewPasscodeFragment,
                                screenType = FeatureSet.CHANGE_PASSCODE
                            )
                        } else
                            getBinding().dialer.startAnimation()
                    }
                }
                R.id.tvForgotPasscode -> {
                    viewModel.createForgotPassCodeOtp { username ->
                        startOtpFragment(username)
                    }
                }
            }
        })
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
        val sharedPreferenceManager = SharedPreferenceManager(requireContext())
        if (viewModel.isUserLoggedIn()) {
            sharedPreferenceManager.getDecryptedUserName()?.let {
                val action =
                    CurrentPasscodeFragmentDirections.actionCurrentPasscodeFragmentToForgotPasscodeNavigation(
                        viewModel.mobileNumber,
                        viewModel.token,
                        Constants.FORGOT_PASSCODE_FROM_CHANGE_PASSCODE
                    )
                navigate(action,screenType = FeatureSet.FORGOT_PASSCODE)
            } ?: showToast("Invalid username found")
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    fun getBinding(): FragmentPassCodeBinding {
        return viewDataBinding as FragmentPassCodeBinding
    }



    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                 activity?.onBackPressed()
             }
        }
    }
}