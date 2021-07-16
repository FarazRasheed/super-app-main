package com.yap.yapandroid.modules.login.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.yap.yapandroid.BR
import com.yap.yapandroid.constants.Constants
import com.yap.yapandroid.modules.login.interfaces.ISystemPermission
import com.yap.yapandroid.modules.login.viewmodels.SystemPermissionViewModel
import co.yap.modules.webview.WebViewFragment
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants.KEY_TOUCH_ID_ENABLED
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.leanplum.KYCEvents
import co.yap.yapcore.leanplum.trackEvent
import co.yap.yapcore.managers.SessionManager
import com.yap.yapandroid.R

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SystemPermissionFragment : BaseBindingFragment<ISystemPermission.ViewModel>(),
    ISystemPermission.View {

    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_biometric_permission

    override val viewModel: ISystemPermission.ViewModel
        get() = ViewModelProviders.of(this).get(SystemPermissionViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferenceManager = SharedPreferenceManager(requireContext())

        viewModel.screenType = getScreenType()
        viewModel.registerLifecycleOwner(this)

        viewModel.permissionGrantedPressEvent.observe(this, permissionGrantedObserver)
        viewModel.permissionNotGrantedPressEvent.observe(this, permissionNotGrantedObserver)
        viewModel.handlePressOnTermsAndConditionsPressEvent.observe(
            this,
            handlePressOnTermsAndConditionsObserver
        )
    }

    override fun onDestroyView() {
        viewModel.permissionGrantedPressEvent.removeObservers(this)
        viewModel.permissionNotGrantedPressEvent.removeObservers(this)
        super.onDestroyView()

    }

    private val permissionGrantedObserver = Observer<Boolean> {
        if (viewModel.screenType == Constants.TOUCH_ID_SCREEN_TYPE) {
            sharedPreferenceManager.save(KEY_TOUCH_ID_ENABLED, true)
            trackEvent(KYCEvents.SIGN_UP_ENABLED_PERMISSION.type,"TouchID")
            val action =
                SystemPermissionFragmentDirections.actionSystemPermissionFragmentToSystemPermissionFragmentNotification(
                    Constants.NOTIFICATION_SCREEN_TYPE
                )
            findNavController().navigate(action)
        } else {
            navigateToDashboard()
        }
    }

    private val permissionNotGrantedObserver = Observer<Boolean> {
        if (viewModel.screenType == Constants.TOUCH_ID_SCREEN_TYPE) {
            sharedPreferenceManager.save(KEY_TOUCH_ID_ENABLED, false)
            val action =
                SystemPermissionFragmentDirections.actionSystemPermissionFragmentToSystemPermissionFragmentNotification(
                    Constants.NOTIFICATION_SCREEN_TYPE
                )
            findNavController().navigate(action)
        } else {
            navigateToDashboard()
        }
    }

    private val handlePressOnTermsAndConditionsObserver = Observer<Int> {
        when (it) {
            R.id.tvTermsAndConditions -> {
                startFragment(
                    fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                        co.yap.yapcore.constants.Constants.PAGE_URL to co.yap.yapcore.constants.Constants.URL_TERMS_CONDITION
                    ), showToolBar = false
                )
            }
        }
    }

    private fun navigateToDashboard() {
        if (SessionManager.user?.otpBlocked == true|| SessionManager.user?.freezeInitiator != null)
            startFragment(fragmentName = OtpBlockedInfoFragment::class.java.name)
        else
            findNavController().navigate(R.id.action_goto_yapDashboardActivity)

        activity?.finish()
    }

    private fun getScreenType(): String {
        return arguments?.let { SystemPermissionFragmentArgs.fromBundle(it).screenType } as String
    }

}
