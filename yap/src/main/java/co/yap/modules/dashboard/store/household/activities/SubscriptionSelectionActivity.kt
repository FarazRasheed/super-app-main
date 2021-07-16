package co.yap.modules.dashboard.store.household.activities

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.modules.dashboard.cards.addpaymentcard.spare.SpareCardsLandingAdapter
import co.yap.modules.dashboard.store.household.interfaces.IHouseHoldSubscription
import co.yap.modules.dashboard.store.household.onboarding.HouseHoldOnboardingActivity
import co.yap.modules.dashboard.store.household.viewmodels.SubscriptionSelectionViewModel
import co.yap.networking.household.responsedtos.HouseHoldPlan
import co.yap.translation.Translator
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.activity_house_hold_subscription_selction.*


class SubscriptionSelectionActivity :
    BaseBindingActivity<IHouseHoldSubscription.ViewModel>(),
    IHouseHoldSubscription.View, SpareCardsLandingAdapter.OnItemClickedListener {

    lateinit var item: View
    var selectedPosition: Int = 0
    var incrementValue: Boolean = true
    var exitEvent: Boolean = false
    var selectedPlan: HouseHoldPlan = HouseHoldPlan()

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_house_hold_subscription_selction

    override val viewModel: IHouseHoldSubscription.ViewModel
        get() = ViewModelProviders.of(this).get(SubscriptionSelectionViewModel::class.java)

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SubscriptionSelectionActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        viewModel.fetchHouseholdPackagesFee()
    }

    override fun onResume() {
        super.onResume()
        addListeners()
    }

    private fun addListeners() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnClose -> {
                    setIntentResult(false)
                }
                R.id.llAnnualSubscription -> {
                    viewModel.state.hasSelectedPackage = true
                    llMonthlySubscription.isActivated = false
                    llAnnualSubscription.isActivated = true
                    if (!viewModel.plansList.isNullOrEmpty())
                        selectedPlan = viewModel.plansList[1]
                }

                R.id.llMonthlySubscription -> {
                    viewModel.state.hasSelectedPackage = true
                    llMonthlySubscription.isActivated = true
                    llAnnualSubscription.isActivated = false
                    if (!viewModel.plansList.isNullOrEmpty())
                        selectedPlan = viewModel.plansList[0]
                }

                R.id.btnGetStarted -> {
                    if (!viewModel.plansList.isNullOrEmpty())
                        startActivityForResult(
                            HouseHoldOnboardingActivity.newIntent(
                                this@SubscriptionSelectionActivity,
                                selectedPlan, viewModel.plansList
                            ), RequestCodes.REQUEST_ADD_HOUSE_HOLD
                        )
                    //confirmationDialog()
                }

                R.id.imgClose -> {
                    setIntentResult(false)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_ADD_HOUSE_HOLD) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val finishScreen =
                        data.getValue(
                            RequestCodes.REQUEST_CODE_FINISH,
                            ExtraType.BOOLEAN.name
                        ) as? Boolean
                    finishScreen?.let { it ->
                        if (it) {
                            setIntentResult(true)
                        } else {
                            // other things?
                        }
                    }
                }
            }
        }
    }

    private fun confirmationDialog() {
        Utils.confirmationDialog(this,
            "${SessionManager.getDefaultCurrency()} 59.99 per month",
            "The monthly plan is a minimum 12 month contract. By choosing this plan you agree to our terms and conditions.",
            Translator.getString(
                this,
                R.string.common_button_confirm
            ),
            Translator.getString(
                this,
                R.string.cancel
            ),
            object : OnItemClickListener {
                override fun onItemClick(view: View, data: Any, pos: Int) {
                    if (data is Boolean) {
                        if (data) {
                            // startActivity(HouseHoldOnboardingActivity.newIntent(this@SubscriptionSelectionActivity, selectedPlan))
                        }
                    }
                }
            })

    }

    override fun onPause() {
        super.onPause()
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onItemClick(benefitsModel: BenefitsModel) {

    }

    private fun initViewPager() {

        val subscriptionPagerAdapter = SubscriptionPagerAdapter(
            context = this,
            contents = viewModel.getPages(),
            layout = R.layout.content_subscription_selection_pager
        )

        //Don't delete it its intentional
        //Don't delete it its intentional
//        welcome_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
////                if (position == 0) {
//                if (!exitEvent) {
//                    exitEvent = true
//                    if (subscriptionPagerAdapter.viewsContainer[position] is View) {
//                        item = subscriptionPagerAdapter.viewsContainer[position] as View
//                        slideInDsscription(
//                            item.tvDescription,
//                            item.ivPoster
//                        )
//                    }
//                }
//                selectedPosition = position
//
//            }
//
//            override fun onPageSelected(position: Int) {
////                if (position != 0) {
//                if (exitEvent) {
//                    if (subscriptionPagerAdapter.viewsContainer[position] is View) {
//                        item = subscriptionPagerAdapter.viewsContainer[position] as View
//
//                        slideInDsscription(
//                            item.tvDescription,
//                            item.ivPoster
//                        )
//                    }
//                }
//
//            }
//
//        })

        welcome_pager?.adapter = subscriptionPagerAdapter
        worm_dots_indicator?.setViewPager(welcome_pager)

        //Don't delete it its intentional
//        welcome_pager!!.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(
//                v: View?,
//                event: MotionEvent?
//            ): Boolean {
//                when (event!!.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        if (event!!.getX() < 490) {
//                            // tapped on left of center
//                            if (welcome_pager.currentItem > 0 && welcome_pager.currentItem <= 2) {
//
//
//                                if (::item.isInitialized) {
//                                    incrementValue = false
//
//                                    return slideOutDsscription(
//                                        item.tvDescription,
//                                        item.ivPoster
//                                    )
//                                }
//                                return true
//
//                            }
//
//                        }
//                        if (event!!.getX() > 490) {
//                            //tapped on right of center
//                            if (welcome_pager.currentItem < 2) {
//
//                                if (::item.isInitialized) {
//                                    incrementValue = true
//                                    return slideOutDsscription(
//                                        item.tvDescription,
//                                        item.ivPoster
//                                    )
//
//                                }
//                                return true
//
//
//                            }
//                        }
//                        return true
//                    }
//
//                }
//
//                return true  // setting up false is necessary to consider swipe
//
//            }
//        })
    }

    fun slideInDsscription(viewSecond: View, viewThird: View) {
        viewThird!!.visibility = View.GONE
        viewSecond!!.visibility = View.GONE

        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideInRight
        } else {
            techniques = Techniques.SlideInLeft

        }

        viewThird!!.visibility = View.GONE
//        viewSecond!!.visibility = View.VISIBLE
        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    viewSecond!!.visibility = View.VISIBLE
                    slideInImage(viewThird)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
//                    slideInImage(viewThird)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(100)
            .repeat(0)
            .playOn(viewSecond)

    }

    fun slideInImage(viewThird: View) {
        var outCheck: Boolean = false
        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideInRight
            outCheck = false
        } else {
            outCheck = true
            techniques = Techniques.SlideInLeft

        }
        viewThird!!.visibility = View.VISIBLE

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
            .duration(100)
            .repeat(0)
            .playOn(viewThird)

    }

    fun slideOutDsscription(viewSecond: View, viewThird: View): Boolean {

        var techniques: Techniques
        if (incrementValue) {
            techniques = Techniques.SlideOutLeft
        } else {
            techniques = Techniques.SlideOutRight

        }

        YoYo.with(techniques)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    viewSecond!!.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    viewSecond!!.visibility = View.GONE
                    slideOutImage(viewThird)

                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(100)
            .repeat(0)
            .playOn(viewSecond)

        return true
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
                    viewThird!!.visibility = View.GONE

                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(100)
            .repeat(0)
            .playOn(viewThird)

    }


    private fun setIntentResult(shouldFinished: Boolean) {
        val intent = Intent()
        intent.putExtra(RequestCodes.REQUEST_CODE_FINISH, shouldFinished)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}