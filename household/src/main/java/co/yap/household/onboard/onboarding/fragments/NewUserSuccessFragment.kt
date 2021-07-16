package co.yap.household.onboard.onboarding.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.household.BR
import co.yap.household.R
import co.yap.household.onboard.cardselection.HouseHoldCardsSelectionActivity
import co.yap.household.onboard.onboarding.interfaces.INewUserSuccess
import co.yap.household.onboard.onboarding.viewmodels.NewUserSuccessViewModel
import co.yap.household.onboard.otherscreens.InvalidEIDActivity
import co.yap.modules.kyc.activities.DocumentsDashboardActivity
import co.yap.modules.onboarding.activities.LiteDashboardActivity
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.AnimationUtils
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_new_user_success.*
import kotlinx.coroutines.delay

class NewUserSuccessFragment :
    OnboardingChildFragment<INewUserSuccess.ViewModel>() {

    private val windowSize: Rect = Rect() // to hold the size of the visible window

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_new_user_success

    override val viewModel: NewUserSuccessViewModel
        get() = ViewModelProviders.of(this).get(NewUserSuccessViewModel::class.java)


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val display = activity!!.windowManager.defaultDisplay
        display.getRectSize(windowSize)

        // hide all in the beginning
        rootContainer.children.forEach { it.alpha = 0f }

        btnCompleteVerifiocation.setOnClickListener {
            launchActivity<DocumentsDashboardActivity>(requestCode = RequestCodes.REQUEST_KYC_DOCUMENTS) {
                putExtra(Constants.name, SessionManager.user?.currentCustomer?.firstName.toString())
                putExtra(Constants.data, false)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.launch {
            delay(500)
            runAnimations()
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


    private fun runAnimations() {
        AnimationUtils.runSequentially(
//            titleAnimation(),
            // Card Animation
            AnimationUtils.outOfTheBoxAnimation(tvTitle),
            // Bottom views animation
            AnimationUtils.runTogether(
                AnimationUtils.jumpInAnimation(tvSubTitle),
                AnimationUtils.jumpInAnimation(ivMobileSuccess).apply { startDelay = 100 },
                AnimationUtils.jumpInAnimation(btnCompleteVerifiocation).apply { startDelay = 200 }/*,
                AnimationUtils.jumpInAnimation(btnCompleteVerification).apply { startDelay = 300 }*/
            )
        ).apply {
            addListener(onEnd = {
                //                setObservers()
            })
        }.start()
    }

    override fun onDestroyView() {
//        viewModel.nextButtonPressEvent.removeObservers(this)
//        viewModel.animationStartEvent.removeObservers(this)
        super.onDestroyView()
    }

    private val nextButtonObserver = Observer<Int> {
        when (it) {
//            viewModel.EVENT_NAVIGATE_NEXT -> navigate(R.id.congratulationsFragment)
//            viewModel.EVENT_POST_VERIFICATION_EMAIL -> viewModel.sendVerificationEmail()
//            viewModel.EVENT_POST_DEMOGRAPHIC -> viewModel.postDemographicData()
        }

    }


    override fun onBackPressed(): Boolean = true
}

