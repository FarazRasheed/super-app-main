package co.yap.modules.dashboard.cards.addpaymentcard.spare.helpers.virtual

import android.animation.Animator
import android.content.Context
import android.view.View
import androidx.navigation.NavController
import co.yap.modules.dashboard.cards.addpaymentcard.activities.AddPaymentCardActivity.Companion.onBackPressCheck
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.IAddSpareCard
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.layout_add_spare_virtaul_card_confirm_purchase.view.*
import kotlinx.android.synthetic.main.layout_add_spare_virtual_card_success.view.*


class AddSpareVirtualCardViewHelper(
    val context: Context, val navController: NavController, val virtualCardView: View,
    val viewModel: IAddSpareCard.ViewModel
) {
    init {

        fadeOutView(virtualCardView.tvTransactionComplete)
        fadeOutView(virtualCardView.flTransactionComplete)
        fadeOutView(virtualCardView.btnDoneAddingSpareVirtualCard)

        virtualCardView.llConfirmVirtualCardPurchase.visibility = View.GONE
        virtualCardView.layoutVirtualCardOnSuccess.visibility = View.VISIBLE
        YoYo.with(Techniques.FadeOut)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    onBackPressCheck=false
                    slideInTitle()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(50)
            .repeat(0)
            .playOn(virtualCardView.llConfirmVirtualCardPurchase)
    }


    fun slideInTitle() {
        YoYo.with(Techniques.BounceInUp)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

//                    virtualCardView.svContainer.fullScroll( ScrollView.FOCUS_DOWN)
//                    virtualCardView.svContainer.smoothScrollBy(0, virtualCardView.svContainer.maxScrollAmount)
//                    Log.i("height",virtualCardView.svContainer.getChildAt(0).height.toString() + " ")
//                    virtualCardView.svContainer.smoothScrollBy(0, virtualCardView.svContainer.getChildAt(0).height)
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    FadeInSuccessImage()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(500)
            .repeat(0)
            .playOn(virtualCardView.tvTransactionComplete)

    }

    fun FadeInSuccessImage() {

        YoYo.with(Techniques.ZoomIn)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    slideInBtn()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
            .duration(700)
            .repeat(0)
            .playOn(virtualCardView.flTransactionComplete)
    }

    fun slideInBtn() {

        YoYo.with(Techniques.BounceInUp)
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
            .duration(500)
            .repeat(0)
            .playOn(virtualCardView.btnDoneAddingSpareVirtualCard)
    }


    fun fadeOutView(virtualCardView: View) {
        YoYo.with(Techniques.FadeOut)
            .duration(50)
            .playOn(virtualCardView)
    }
}