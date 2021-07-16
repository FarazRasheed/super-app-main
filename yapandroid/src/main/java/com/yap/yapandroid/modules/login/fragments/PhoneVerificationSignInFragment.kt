package com.yap.yapandroid.modules.login.fragments

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import com.yap.yapandroid.constants.Constants
import com.yap.yapandroid.modules.login.interfaces.IPhoneVerificationSignIn
import com.yap.yapandroid.modules.login.viewmodels.PhoneVerificationSignInViewModel
import co.yap.household.onboard.onboarding.main.OnBoardingHouseHoldActivity
import co.yap.modules.autoreadsms.MySMSBroadcastReceiver
import co.yap.modules.onboarding.enums.AccountType
import co.yap.networking.customers.responsedtos.AccountInfo
import co.yap.yapcore.constants.Constants.SMS_CONSENT_REQUEST
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.biometric.BiometricUtil
import co.yap.yapcore.helpers.extentions.getOtpFromMessage
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.helpers.extentions.startSmsConsent
import co.yap.yapcore.managers.SessionManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.yap.yapandroid.R
import com.yap.yapandroid.main.MainChildFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhoneVerificationSignInFragment :
    MainChildFragment<IPhoneVerificationSignIn.ViewModel>(), IPhoneVerificationSignIn.View {
    private var intentFilter: IntentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
    private var appSMSBroadcastReceiver: MySMSBroadcastReceiver? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_phone_verification

    override val viewModel: PhoneVerificationSignInViewModel
        get() = ViewModelProviders.of(this).get(PhoneVerificationSignInViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.reverseTimer(10, requireContext())
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        getData()
    }

    private val stateObserver = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (viewModel.isValidOtpLength(viewModel.state.otp.get() ?: "")) {
                viewModel.clickEvent.call()
            }
        }
    }

    override fun setObservers() {
        viewModel.state.otp.addOnPropertyChangedCallback(stateObserver)
        context?.startSmsConsent()
        initBroadcast()
        context?.registerReceiver(appSMSBroadcastReceiver, intentFilter)
        viewModel.clickEvent.observe(this, Observer {
            viewModel.verifyOtp()
        })
        viewModel.postDemographicDataResult.observe(this, postDemographicDataObserver)
        viewModel.accountInfo.observe(this, onFetchAccountInfo)
    }

    private fun initBroadcast() {
        appSMSBroadcastReceiver =
            MySMSBroadcastReceiver(object : MySMSBroadcastReceiver.OnSmsReceiveListener {
                override fun onReceive(code: Intent?) {
                    startActivityForResult(code, SMS_CONSENT_REQUEST)
                }
            })
    }

    private val postDemographicDataObserver = Observer<Boolean> {
        viewModel.getAccountInfo()
    }
    private val onFetchAccountInfo = Observer<AccountInfo> {
        it?.run {
            SessionManager.updateCardBalance {  }
            if (accountType == AccountType.B2C_HOUSEHOLD.name) {
                val bundle = Bundle()
                SharedPreferenceManager(requireContext()).setThemeValue(co.yap.yapcore.constants.Constants.THEME_HOUSEHOLD)
                bundle.putBoolean(OnBoardingHouseHoldActivity.EXISTING_USER, false)
                bundle.putParcelable(OnBoardingHouseHoldActivity.USER_INFO, it)
                startActivity(OnBoardingHouseHoldActivity.getIntent(requireContext(), bundle))
                activity?.finish()
            } else {
                if (BiometricUtil.hasBioMetricFeature(requireActivity())
                ) {
                    if (SharedPreferenceManager(requireContext()).getValueBoolien(
                            co.yap.yapcore.constants.Constants.KEY_TOUCH_ID_ENABLED,
                            false
                        )
                    ) {
                        if (it.otpBlocked == true || SessionManager.user?.freezeInitiator != null)
                            startFragment(fragmentName = OtpBlockedInfoFragment::class.java.name)
                        else
                            findNavController().navigate(R.id.action_goto_yapDashboardActivity)

                        activity?.finish()
                    } else {
                        val action =
                            PhoneVerificationSignInFragmentDirections.actionPhoneVerificationSignInFragmentToSystemPermissionFragment(
                                Constants.TOUCH_ID_SCREEN_TYPE
                            )
                        findNavController().navigate(action)
                    }

                } else {
                    if (it.otpBlocked == true || SessionManager.user?.freezeInitiator != null)
                        startFragment(fragmentName = OtpBlockedInfoFragment::class.java.name)
                    else
                        findNavController().navigate(R.id.action_goto_yapDashboardActivity)

                    activity?.finish()
                }
            }
        }
    }

    private fun getData() {
        viewModel.state.username =
            arguments?.let { PhoneVerificationSignInFragmentArgs.fromBundle(it).username } as String
        viewModel.state.passcode =
            arguments?.let { PhoneVerificationSignInFragmentArgs.fromBundle(it).passcode } as String
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    viewModel.state.otp.set(context?.getOtpFromMessage(message ?: "") ?: "")
                }
        }
    }

    override fun removeObservers() {
        viewModel.state.otp.removeOnPropertyChangedCallback(stateObserver)
        viewModel.postDemographicDataResult.removeObservers(this)
        viewModel.accountInfo.removeObservers(this)
        viewModel.clickEvent.removeObservers(this)
        context?.unregisterReceiver(appSMSBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                Utils.hideKeyboard(requireView())
                GlobalScope.launch(Dispatchers.Main) {
                    delay(100)
                    navigateBack()
                }
            }
        }
    }
}