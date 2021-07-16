package com.yap.yapandroid.modules.startup.fragments

import android.animation.Animator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import co.yap.BR
import com.yap.yapandroid.modules.startup.adapters.WelcomePagerAdapter
import co.yap.modules.onboarding.enums.AccountType
import co.yap.modules.onboarding.interfaces.IWelcome
import co.yap.modules.onboarding.viewmodels.WelcomeViewModel
import co.yap.yapcore.BaseBindingFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.yap.yapandroid.R
import kotlinx.android.synthetic.main.fragment_onboarding_welcome.*


open class WelcomeFragment : BaseBindingFragment<IWelcome.ViewModel>(), IWelcome.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_onboarding_welcome

    override val viewModel: IWelcome.ViewModel
        get() = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)

    lateinit var item: View
    lateinit var olditem: View
    var selectedPosition: Int = 0
    var newPosition: Int = 0
    var incrementValue: Boolean = true
    var exitEvent: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.accountType = getAccountType()

        val welcomePagerAdapter = WelcomePagerAdapter(
            context = requireContext(),
            contents = viewModel.getPages(),
            layout = R.layout.content_onboarding_welcome
        )

        viewModel.onGetStartedPressEvent.observe(this, getStartedButtonObserver)

        welcome_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                if (position == 0) {
                if (!exitEvent) {
                    exitEvent = true
                    if (welcomePagerAdapter.viewsContainer[position] is View) {
                        item = welcomePagerAdapter.viewsContainer[position] as View
                        slideInTitle(
                            item.findViewById<TextView>(R.id.tvTitle),
                            item.findViewById<TextView>(R.id.tvDescription),
                            item.findViewById<ImageView>(R.id.ivPoster)
                        )
                    }
                }
                selectedPosition = position

            }

            override fun onPageSelected(position: Int) {
//                if (position != 0) {
                if (exitEvent) {
                    if (welcomePagerAdapter.viewsContainer[position] is View) {
                        item = welcomePagerAdapter.viewsContainer[position] as View

                        slideInTitle(
                            item.findViewById<TextView>(R.id.tvTitle),
                            item.findViewById<TextView>(R.id.tvDescription),
                            item.findViewById<ImageView>(R.id.ivPoster)
                        )
                    }
                }

            }

        })

        welcome_pager?.adapter = welcomePagerAdapter
        view.findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)?.setViewPager(welcome_pager)

        welcome_pager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(
                v: View?,
                event: MotionEvent?
            ): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (event.x < 490) {
                            // tapped on left of center
                            if (welcome_pager.currentItem in 1..2) {
                                if (::item.isInitialized) {
                                    incrementValue = false

                                    return slideOutTitle(
                                        item.findViewById<TextView>(R.id.tvTitle),
                                        item.findViewById<TextView>(R.id.tvDescription),
                                        item.findViewById<ImageView>(R.id.ivPoster)
                                    )
                                }
                                return true

                            }

                        }
                        if (event.x > 490) {
                            //tapped on right of center
                            if (welcome_pager.currentItem < 2) {

                                if (::item.isInitialized) {
                                    incrementValue = true
                                    return slideOutTitle(
                                        item.findViewById<TextView>(R.id.tvTitle),
                                        item.findViewById<TextView>(R.id.tvDescription),
                                        item.findViewById<ImageView>(R.id.ivPoster)
                                    )

                                }
                                return true


                            }
                        }
                        return true
                    }

                }

                return true  // setting up false is necessary to consider swipe

            }
        })

    }


    override fun onDestroyView() {
        viewModel.onGetStartedPressEvent.removeObservers(this)
        super.onDestroyView()
    }

    private val getStartedButtonObserver = Observer<Boolean> {
        findNavController().navigate(R.id.action_welcomeFragment_to_onboardingActivity, arguments)
    }

    private fun getAccountType(): AccountType =
        arguments?.getSerializable(getString(R.string.arg_account_type)) as AccountType


    fun slideInTitle(viewFirst: View, viewSecond: View, viewThird: View) {
        viewFirst.visibility = VISIBLE

        val techniques: Techniques = if (incrementValue) {
            Techniques.SlideInRight
        } else {
            Techniques.SlideInLeft

        }

        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    slideInDsscription(viewSecond, viewThird)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(400)
            .repeat(0)
            .playOn(viewFirst)

    }

    fun slideInDsscription(viewSecond: View, viewThird: View) {
        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideInRight
        } else {
            techniques = Techniques.SlideInLeft

        }

        viewSecond.visibility = VISIBLE

        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    slideInImage(viewThird)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(200)
            .repeat(0)
            .playOn(viewSecond)

    }

    fun slideInImage(viewThird: View) {
        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideInRight
        } else {
            techniques = Techniques.SlideInLeft

        }
        viewThird.visibility = VISIBLE

        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(200)
            .repeat(0)
            .playOn(viewThird)

    }


    fun slideOutTitle(viewFirst: View, viewSecond: View, viewThird: View): Boolean {

        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideOutLeft
        } else {
            techniques = Techniques.SlideOutRight

        }
        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    slideOutDsscription(viewSecond, viewThird)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(400)
            .repeat(0)
            .playOn(viewFirst)
        return true
    }

    fun slideOutDsscription(viewSecond: View, viewThird: View) {

        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideOutLeft
        } else {
            techniques = Techniques.SlideOutRight

        }
        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    slideOutImage(viewThird)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(200)
            .repeat(0)
            .playOn(viewSecond)

    }

    fun slideOutImage(viewThird: View) {

        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideOutLeft
        } else {
            techniques = Techniques.SlideOutRight

        }
        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (incrementValue) {
                        welcome_pager.setCurrentItem(welcome_pager.currentItem + 1)
                    } else {
                        welcome_pager.setCurrentItem(welcome_pager.currentItem - 1)
                    }

                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(200)
            .repeat(0)
            .playOn(viewThird)

    }
}