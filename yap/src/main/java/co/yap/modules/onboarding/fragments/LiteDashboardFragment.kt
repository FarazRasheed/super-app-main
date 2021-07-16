package co.yap.modules.onboarding.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.modules.dashboard.main.fragments.YapDashboardChildFragment
import co.yap.modules.dummy.ActivityNavigator
import co.yap.modules.dummy.NavigatorProvider
import co.yap.modules.kyc.activities.DocumentsDashboardActivity
import co.yap.modules.onboarding.constants.Constants
import co.yap.modules.onboarding.interfaces.ILiteDashboard
import co.yap.modules.onboarding.viewmodels.LiteDashboardViewModel
import co.yap.yapcore.constants.Constants.KEY_IS_FINGERPRINT_PERMISSION_SHOWN
import co.yap.yapcore.constants.Constants.KEY_TOUCH_ID_ENABLED
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.biometric.BiometricUtil
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_lite_dashboard.*


class LiteDashboardFragment : YapDashboardChildFragment<ILiteDashboard.ViewModel>() {

    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var mNavigator: ActivityNavigator
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = co.yap.R.layout.fragment_lite_dashboard

    override val viewModel: ILiteDashboard.ViewModel
        get() = ViewModelProviders.of(this).get(LiteDashboardViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mNavigator = (activity?.applicationContext as NavigatorProvider).provideNavigator()
        sharedPreferenceManager = SharedPreferenceManager(requireContext())
        if (BiometricUtil.isFingerprintSupported
            && BiometricUtil.isHardwareSupported(requireContext())
            && BiometricUtil.isPermissionGranted(requireContext())
            && BiometricUtil.isFingerprintAvailable(requireContext())
        ) {
            val isTouchIdEnabled: Boolean =
                sharedPreferenceManager.getValueBoolien(
                    KEY_TOUCH_ID_ENABLED,
                    false
                )
            swTouchId.isChecked = isTouchIdEnabled
            swTouchId.visibility = View.VISIBLE

            swTouchId.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    sharedPreferenceManager.save(
                        KEY_IS_FINGERPRINT_PERMISSION_SHOWN,
                        true
                    )
                    sharedPreferenceManager.save(KEY_TOUCH_ID_ENABLED, true)
                } else {
                    sharedPreferenceManager.save(
                        KEY_TOUCH_ID_ENABLED,
                        false
                    )
                }
            }
        } else {
            swTouchId.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        viewModel.clickEvent.observe(this, observer)
        super.onResume()
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    private val observer = Observer<Int> {
        when (it) {
            viewModel.EVENT_LOGOUT_SUCCESS -> {
                SessionManager.doLogout(requireContext())
                activity?.finish()
            }

            viewModel.EVENT_PRESS_COMPLETE_VERIFICATION -> {
                launchActivity<DocumentsDashboardActivity>(requestCode = RequestCodes.REQUEST_KYC_DOCUMENTS) {
                    putExtra(
                        co.yap.yapcore.constants.Constants.name,
                        SessionManager.user?.currentCustomer?.firstName.toString()
                    )
                    putExtra(co.yap.yapcore.constants.Constants.data, false)
                }
            }
            viewModel.EVENT_PRESS_SET_CARD_PIN -> {
                viewModel.getDebitCards()
            }
            viewModel.EVENT_GET_ACCOUNT_INFO_SUCCESS -> {
                checkUserStatus()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_KYC_DOCUMENTS && resultCode == Activity.RESULT_OK) {
            data?.let {
                val result = data.getBooleanExtra(co.yap.yapcore.constants.Constants.result, false)
                val error = data.getBooleanExtra("error", false)
                if (!result && error) {
                    mNavigator.startEIDNotAcceptedActivity(requireActivity())

                } else {
                    btnCompleteVerification.visibility = if (result) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun checkUserStatus() {
        when (SessionManager.user?.notificationStatuses) {
            Constants.USER_STATUS_ON_BOARDED -> {
                btnCompleteVerification.visibility = View.VISIBLE
                btnSetCardPin.visibility = View.GONE
            }
            Constants.USER_STATUS_MEETING_SUCCESS -> {
                btnSetCardPin.visibility = View.VISIBLE
                btnCompleteVerification.visibility = View.GONE
            }
            Constants.USER_STATUS_MEETING_SCHEDULED -> {
                btnSetCardPin.visibility = View.GONE
                btnCompleteVerification.visibility = View.GONE
            }
            co.yap.yapcore.constants.Constants.USER_STATUS_CARD_ACTIVATED -> {
                btnSetCardPin.visibility = View.GONE
                btnCompleteVerification.visibility = View.GONE
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("CONFIRM") { dialog, which ->
                activity?.finish()
            }
            .setNegativeButton("CANCEL") { dialog, which ->

            }
            .show()
    }

    override fun onBackPressed(): Boolean = showLogoutDialog().let { true }


}