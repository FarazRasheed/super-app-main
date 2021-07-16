package co.yap.widgets.arcmenu.animation

import android.animation.Animator
import android.graphics.Point
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Keep
import co.yap.widgets.arcmenu.FloatingActionMenu

abstract class MenuAnimationHandler {

    // There are only two distinct animations at the moment.
    @Keep
    enum class ActionType {
        OPENING, CLOSING
    }

    var menu: FloatingActionMenu? = null

    /**
     * Starts the opening animation
     * Should be overriden by children
     * @param center
     */
    open fun animateMenuOpening(center: Point) {
        if (menu == null) {
            throw NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.")
        }

    }

    /**
     * Restores the specified sub action view to its final state, according to the current actionType
     * Should be called after an animation finishes.
     * @param subActionItem
     * @param actionType
     */

    protected fun restoreSubActionViewAfterAnimation(
        subActionItem: FloatingActionMenu.Item,
        actionType: ActionType
    ) {
        val params = subActionItem.view.layoutParams
        subActionItem.view.translationX = 0f
        subActionItem.view.translationY = 0f
        subActionItem.view.rotation = 0f
        subActionItem.view.scaleX = 1f
        subActionItem.view.scaleY = 1f
        subActionItem.view.alpha = 1f
        if (actionType == ActionType.OPENING) {
            val lp = params as FrameLayout.LayoutParams

            lp.setMargins(subActionItem.x, subActionItem.y, 0, 0)

            subActionItem.view.layoutParams = lp
        } else if (actionType == ActionType.CLOSING) {
            val center = menu?.actionViewCenter
            val lp = params as FrameLayout.LayoutParams

            lp.setMargins(
                center!!.x - subActionItem.width / 2,
                center.y - subActionItem.height / 2,
                0,
                0
            )

            subActionItem.view.layoutParams = lp
            menu!!.removeViewFromCurrentContainer(subActionItem.view)

//            if (menu!!.isSystemOverlay) {
//                // When all the views are removed from the overlay container,
//                // we also need to detach it
//                if (menu!!.overlayContainer!!.childCount == 0) {
//                    menu!!.detachOverlayContainer()
//                }
//            }
        }
    }

    /**
     * Ends the opening animation
     * Should be overriden by children
     * @param center
     */
    open fun animateMenuClosing(center: Point) {
        if (menu == null) {
            throw NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.")
        }
    }

    inner class LastAnimationListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            setAnimating(true)
        }

        override fun onAnimationEnd(animation: Animator?) {
            setAnimating(false)
            menu!!.mainActionView.isClickable = true
//            if (menu!!.isOpen) {
//                menu!!.alphaOverlay?.visibility = View.GONE
//            }
        }

        override fun onAnimationCancel(animation: Animator?) {
            setAnimating(false)
            menu!!.mainActionView.isClickable = true
        }

        override fun onAnimationStart(animation: Animator?) {
            setAnimating(true)
        }
    }

    abstract fun isAnimating(): Boolean
    abstract fun setAnimating(animating: Boolean)
}