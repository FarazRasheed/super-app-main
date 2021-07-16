package co.yap.widgets.arcmenu.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Point
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import co.yap.widgets.arcmenu.FloatingActionMenu

class DefaultAnimationHandler : MenuAnimationHandler() {

    /** duration of animations, in milliseconds  */
    protected val DURATION = 500
    /** duration to wait between each of   */
    protected val LAG_BETWEEN_ITEMS = 20
    /** holds the current state of animation  */
    private var animating: Boolean = false

    init {
        setAnimating(false)
    }

    override fun isAnimating() = animating

    override fun setAnimating(animating: Boolean) {
        this.animating = animating;
    }

    override fun animateMenuOpening(center: Point) {
        super.animateMenuOpening(center)


        setAnimating(true)

        var lastAnimation: Animator? = null
        for (i in menu!!.subActionItems.indices) {

            menu!!.subActionItems[i].view.scaleX = 0F
            menu!!.subActionItems[i].view.scaleY = 0F
            menu!!.subActionItems[i].view.alpha = 0F

            val pvhX = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_X,
                (menu!!.subActionItems[i].x - center.x + menu!!.subActionItems[i].width / 2).toFloat()
            )
            val pvhY = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_Y,
                (menu!!.subActionItems[i].y - center.y + menu!!.subActionItems[i].height / 2).toFloat()
            )
            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 720f)
            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1f)

            val animation = ObjectAnimator.ofPropertyValuesHolder(
                menu!!.subActionItems[i].view,
                pvhX,
                pvhY,
                pvhR,
                pvhsX,
                pvhsY,
                pvhA
            )
            animation.duration = DURATION.toLong()
            animation.interpolator = OvershootInterpolator(0.9f)
            animation.addListener(
                SubActionItemAnimationListener(
                    menu!!.subActionItems[i],
                    ActionType.OPENING
                )
            )

            if (i == 0) {
                lastAnimation = animation
            }

            // Put a slight lag between each of the menu items to make it asymmetric
            animation.startDelay = ((menu!!.subActionItems.size - i) * LAG_BETWEEN_ITEMS).toLong()
            animation.start()
        }
        lastAnimation?.addListener(LastAnimationListener())
    }

    override fun animateMenuClosing(center: Point) {
        super.animateMenuClosing(center)
        setAnimating(true)

        var lastAnimation: Animator? = null
        for (i in menu!!.subActionItems.indices) {
            val pvhX = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_X,
                -(menu!!.subActionItems.get(i).x - center.x + menu!!.subActionItems[i].width / 2).toFloat()
            )
            val pvhY = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_Y,
                -(menu!!.subActionItems.get(i).y - center.y + menu!!.subActionItems.get(i).height / 2).toFloat()
            )
            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, -720f)
            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)
            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)
            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)

            val animation = ObjectAnimator.ofPropertyValuesHolder(
                menu!!.subActionItems[i].view,
                pvhX,
                pvhY,
                pvhR,
                pvhsX,
                pvhsY,
                pvhA
            )
            animation.duration = DURATION.toLong()
            animation.interpolator = AccelerateDecelerateInterpolator()
            animation.addListener(
                SubActionItemAnimationListener(
                    menu!!.subActionItems.get(i),
                    ActionType.CLOSING
                )
            )

            if (i == 0) {
                lastAnimation = animation
            }

            animation.startDelay = ((menu!!.subActionItems.size - i) * LAG_BETWEEN_ITEMS).toLong()
            animation.start()
        }
        lastAnimation?.addListener(LastAnimationListener())
    }

    internal inner class SubActionItemAnimationListener(
        private val subActionItem: FloatingActionMenu.Item,
        private val actionType: ActionType
    ) :
        Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType)
        }

        override fun onAnimationCancel(animation: Animator) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType)
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }
}