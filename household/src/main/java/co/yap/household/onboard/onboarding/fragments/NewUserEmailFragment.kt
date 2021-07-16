package co.yap.household.onboard.onboarding.fragments

import android.animation.AnimatorSet
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.household.BR
import co.yap.household.R
import co.yap.household.onboard.onboarding.main.OnBoardingHouseHoldActivity
import co.yap.household.onboard.onboarding.interfaces.IEmail
import co.yap.household.onboard.onboarding.viewmodels.EmailHouseHoldViewModel
import co.yap.widgets.AnimatingProgressBar
import co.yap.yapcore.enums.NotificationStatus
import co.yap.yapcore.helpers.AnimationUtils
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.managers.SessionManager

class NewUserEmailFragment : OnboardingChildFragment<IEmail.ViewModel>() {

    private val windowSize: Rect = Rect() // to hold the size of the visible window

    override fun getBindingVariable(): Int = BR.emailHouseHoldViewModel

    override fun getLayoutId(): Int = R.layout.fragment_new_user_email

    override val viewModel: IEmail.ViewModel
        get() = ViewModelProviders.of(this).get(EmailHouseHoldViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObservers()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val display = activity!!.windowManager.defaultDisplay
        display.getRectSize(windowSize)
    }

    private fun addObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.animationStartEvent.observe(this, Observer {
            if (it)
                startAnimation()
        })
        viewModel.onEmailVerifySuccess.observe(this, Observer {
            if (it) {
                viewModel.postDemographicData()
            }
        })
    }


    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.next_button -> {
                hideKeyboard()
                if (viewModel.hasDoneAnimation) {
                    SessionManager.user?.notificationStatuses = NotificationStatus.ON_BOARDED.name
                    findNavController().navigate(R.id.action_emailHouseHoldFragment_to_newUserCongratulationsFragment)
                } else {
                    viewModel.sendVerificationEmail()
                }
            }
        }
    }

    private fun hideKeyboard() {
        try {
            Utils.hideKeyboard(requireView())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAnimation() {
        viewModel.hasDoneAnimation = true
        viewModel.stopTimer()
        Handler(Looper.getMainLooper()).postDelayed({
            toolbarAnimation().apply {
                addListener(onEnd = {
                })
            }.start()
        }, 500)
    }

    private fun toolbarAnimation(): AnimatorSet {
        val checkButton =
            (activity as OnBoardingHouseHoldActivity).findViewById<ImageView>(R.id.tbBtnCheck)
        val backButton =
            (activity as OnBoardingHouseHoldActivity).findViewById<ImageView>(R.id.tbBtnBack)
        val progressbar =
            (activity as OnBoardingHouseHoldActivity).findViewById<AnimatingProgressBar>(R.id.tbProgressBar)

        val checkBtnEndPosition = (windowSize.width() / 2) - (checkButton.width / 2)

        checkButton.isEnabled = true
        return AnimationUtils.runSequentially(
            AnimationUtils.pulse(checkButton),
            AnimationUtils.runTogether(
                AnimationUtils.fadeOut(backButton, 200),
                AnimationUtils.fadeOut(progressbar, 200)
            ),
            AnimationUtils.slideHorizontal(
                view = checkButton,
                from = checkButton.x,
                to = checkBtnEndPosition.toFloat(),
                duration = 500
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.animationStartEvent.removeObservers(this)
        viewModel.onEmailVerifySuccess.removeObservers(this)
    }

    override fun onBackPressed(): Boolean = true
}

