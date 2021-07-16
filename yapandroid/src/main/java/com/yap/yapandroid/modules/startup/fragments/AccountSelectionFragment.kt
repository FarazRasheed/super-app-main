package com.yap.yapandroid.modules.startup.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.yap.yapandroid.modules.startup.interfaces.IAccountSelection
import com.yap.yapandroid.modules.startup.viewmodels.AccountSelectionViewModel
import co.yap.modules.onboarding.enums.AccountType
import co.yap.widgets.video.ExoPlayerCallBack
import co.yap.yapcore.BaseBindingFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.yap.yapandroid.R
import com.yap.yapandroid.BR
import kotlinx.android.synthetic.main.fragment_account_selection.*

class AccountSelectionFragment : BaseBindingFragment<IAccountSelection.ViewModel>(),
    IAccountSelection.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_account_selection
    var captionsIndex: Int = 0
    var isPaused = false
    var isVideoFinished = false
    private var animatorSet: AnimatorSet? = null
    val handler = Handler()
    private var captions = listOf(
        "Bank your way", "Get an account in seconds", "Money transfers made simple",
        "Track your spending", "Split bills effortlessly", "Spend locally wherever you go",
        "Instant spending notifications", "An app for everyone"
    )
    private var captionDelays = listOf(1800, 1000, 1800, 1800, 2500, 1800, 2800, 3000)
    override val viewModel: IAccountSelection.ViewModel
        get() = ViewModelProviders.of(this).get(AccountSelectionViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayer()
    }

    private fun setupPlayer() {
        andExoPlayerView.setSource(R.raw.yap_demo_intro)
        captionsIndex = 0
        handler.postDelayed(runnable, 1000)
        andExoPlayerView.setExoPlayerCallBack(object : ExoPlayerCallBack {
            override fun onError() {
                handler.removeCallbacks(runnable)
                andExoPlayerView.setSource(R.raw.demo_test)
                captionsIndex = 0
                handler.postDelayed(runnable, 1000)
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onPositionDiscontinuity(reason: Int) {
                animatorSet?.cancel()
                animatorSet = null
                //captionsIndex = 0
                tvCaption?.postDelayed({
                    captionsIndex = 0
                    playCaptionAnimation()
                }, 1800)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    captionsIndex = 0
                    tvCaption?.postDelayed({
                        playCaptionAnimation()
                    }, 1800)
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }
        })
    }

    private val runnable = Runnable {
        layoutButtons?.let {
            YoYo.with(Techniques.FadeIn).duration(1500)
                .onStart { layoutButtons?.visibility = View.VISIBLE }.playOn(layoutButtons)
        }
    }
    private val layoutButtonsRunnable = Runnable {
        layoutButtons?.let { playCaptionAnimation() }
    }

    fun playCaptionAnimation() {
        tvCaption?.let {
            if (!isPaused && captionsIndex != -1) {
                tvCaption?.text = captions[captionsIndex]
                val fadeIn = ObjectAnimator.ofFloat(
                    it,
                    View.ALPHA,
                    0f, 1f
                )
                fadeIn.interpolator = DecelerateInterpolator() //add this
                fadeIn.duration = 400
                val fadeOut = ObjectAnimator.ofFloat(
                    it,
                    View.ALPHA,
                    1f, 0f
                )
                fadeOut.interpolator = AccelerateInterpolator() //add this
                fadeOut.duration = 400
                fadeOut.startDelay = captionDelays[captionsIndex].toLong()
                animatorSet = AnimatorSet()
                animatorSet?.interpolator = AccelerateDecelerateInterpolator()
                animatorSet?.playSequentially(fadeIn, fadeOut)
                animatorSet?.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        captionsIndex += 1
                        if (captionsIndex < captions.size) {
                            playCaptionAnimation()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        captionsIndex = 0
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        tvCaption?.visibility = View.VISIBLE
                    }
                })
                animatorSet?.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        animatorSet?.resume()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tvSignIn -> {
                    findNavController().navigate(R.id.action_accountSelectionFragment_to_loginFragment)
                }
                R.id.btnPersonal -> {
                    findNavController().navigate(
                        R.id.action_accountSelectionFragment_to_welcomeFragment,
                        Bundle().apply {
                            putSerializable(
                                getString(R.string.arg_account_type),
                                AccountType.B2C_ACCOUNT
                            )
                        })
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        animatorSet?.pause()
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        animatorSet?.cancel()
        animatorSet = null
        captionsIndex = -1
        isPaused = false
        isVideoFinished = false
    }
}