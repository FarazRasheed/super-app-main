package co.yap.modules.onboarding.fragments

import android.animation.AnimatorSet
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.onboarding.activities.OnboardingActivity
import co.yap.modules.onboarding.interfaces.IWaitingList
import co.yap.modules.onboarding.viewmodels.WaitingListViewModel
import co.yap.modules.webview.WebViewFragment
import co.yap.widgets.AnimatingProgressBar
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.AnimationUtils
import co.yap.yapcore.helpers.extentions.startFragment

class WaitingListFragment : OnboardingChildFragment<IWaitingList.ViewModel>(), IWaitingList.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_waiting_list
    private val windowSize: Rect = Rect()

    override val viewModel: IWaitingList.ViewModel
        get() = ViewModelProviders.of(this).get(WaitingListViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val display = activity?.windowManager?.defaultDisplay
        display?.getRectSize(windowSize)
        startAnimation()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
    }

    var clickEvent = Observer<Int> {
        when (it) {
            R.id.btnKeepMePosted -> {
                startFragment(
                    fragmentName = WebViewFragment::class.java.name,
                    clearAllPrevious = true,
                    bundle = bundleOf(
                        Constants.PAGE_URL to Constants.URL_YAP_WEBSITE
                    ),
                    showToolBar = false
                )
//                showToast(getString(Strings.screen_waiting_list_display_dialog_text) + "^" + AlertType.DIALOG_WITH_CUSTOM_BUTTON_TEXT)
            }
        }
    }

    private fun startAnimation() {
        Handler(Looper.getMainLooper()).postDelayed({
            toolbarAnimation().apply {
                addListener(onEnd = {
                })
            }.start()
        }, 500)
    }

    private fun toolbarAnimation(): AnimatorSet {
        val checkButton = (activity as OnboardingActivity).findViewById<ImageView>(R.id.tbBtnCheck)
        val backButton = (activity as OnboardingActivity).findViewById<ImageView>(R.id.tbBtnBack)
        val progressbar =
            (activity as OnboardingActivity).findViewById<AnimatingProgressBar>(R.id.tbProgressBar)
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

    override fun removeObservers() {
        viewModel.clickEvent.removeObservers(this)
    }
}
