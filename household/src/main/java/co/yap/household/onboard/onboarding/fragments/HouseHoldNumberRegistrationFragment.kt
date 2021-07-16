package co.yap.household.onboard.onboarding.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.household.BR
import co.yap.household.R
import co.yap.household.dashboard.main.activities.HouseholdDashboardActivity
import co.yap.household.onboard.cardselection.HouseHoldCardsSelectionActivity
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldNumberRegistration
import co.yap.household.onboard.onboarding.viewmodels.HouseHoldNumberRegistrationViewModel
import co.yap.household.onboard.otherscreens.InvalidEIDActivity
import co.yap.modules.onboarding.activities.LiteDashboardActivity
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.enums.NotificationStatus
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import kotlinx.android.synthetic.main.fragment_house_hold_number_registration.*


class HouseHoldNumberRegistrationFragment :
    OnboardingChildFragment<IHouseHoldNumberRegistration.ViewModel>(),
    IHouseHoldNumberRegistration.View {
    override fun getBindingVariable(): Int = BR.houseHoldViewModel

    override fun getLayoutId(): Int = R.layout.fragment_house_hold_number_registration

    override val viewModel: HouseHoldNumberRegistrationViewModel
        get() = ViewModelProviders.of(this).get(HouseHoldNumberRegistrationViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.parentViewModel?.state?.accountInfo?.run {
            if (!notificationStatuses.isNullOrBlank())
                when (NotificationStatus.valueOf(notificationStatuses)) {
                    NotificationStatus.PARNET_MOBILE_VERIFICATION_PENDING -> {

                    }
                    NotificationStatus.PASS_CODE_PENDING -> {
                        findNavController().navigate(R.id.to_houseHoldCreatePassCodeFragment)
                    }
                    NotificationStatus.EMAIL_PENDING -> {
                        findNavController().navigate(R.id.action_houseHoldNumberRegistrationFragment_to_emailHouseHoldFragment)
                    }
                    else -> {
                        startActivity(
                            Intent(
                                requireContext(),
                                HouseholdDashboardActivity::class.java
                            )
                        )
                        activity?.finish()
                    }
                }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_KYC_DOCUMENTS) {
                data?.let {
                    val success =
                        data.getValue(
                            Constants.result,
                            ExtraType.BOOLEAN.name
                        ) as? Boolean
                    val skipped =
                        data.getValue(
                            Constants.skipped,
                            ExtraType.BOOLEAN.name
                        ) as? Boolean

                    success?.let {
                        if (it) {
                            startActivity(
                                HouseHoldCardsSelectionActivity.newIntent(
                                    requireContext(),
                                    false
                                )
                            )
                            activity?.finish()
                        } else {
                            skipped?.let { skip ->
                                if (skip) {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            LiteDashboardActivity::class.java
                                        )
                                    )

                                } else {
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            InvalidEIDActivity::class.java
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialer.setInPutEditText(etPhoneNumber)
        dialer.hideFingerprintView()
    }

    private val isParentMobileValid = Observer<Boolean>
    {
        if (it) {
            findNavController().navigate(R.id.to_houseHoldCreatePassCodeFragment)
        }
    }

    override fun setObservers() {
        viewModel.clickEvent?.observe(this, Observer { it ->
            when (it) {
                R.id.btnConfirm -> {
                    viewModel.state.existingYapUser?.let {
                        if (it) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    HouseHoldCardsSelectionActivity::class.java
                                )
                            )
                        } else {
                            viewModel.verifyHouseholdParentMobile()
                            //
                        }

                    }

                }
            }
        })
        viewModel.isParentMobileValid?.observe(this, isParentMobileValid)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent?.removeObservers(this)
        viewModel.isParentMobileValid?.removeObservers(this)
    }

    override fun onBackPressed(): Boolean = false
}