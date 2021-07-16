package co.yap.widgets.guidedtour.description

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation

object TourUtils {

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun getAxisXpositionOfViewOnScreen(targetView: View): Int {
        val locationTarget = IntArray(2)
        targetView.getLocationOnScreen(locationTarget)
        return locationTarget[0]
    }

    fun getAxisYpositionOfViewOnScreen(targetView: View): Int {
        val locationTarget = IntArray(2)
        targetView.getLocationOnScreen(locationTarget)
        return locationTarget[1]
    }


    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun dpToPx(dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        return Math.round(dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun isViewLocatedAtHalfTopOfTheScreen(context: Context, targetView: View): Boolean {
        val screenHeight = getScreenHeight(context)
        val positionTargetAxisY = getAxisYpositionOfViewOnScreen(targetView)
        return screenHeight / 2 > positionTargetAxisY
    }

    fun isViewLocatedAtBottomOfTheScreen(
        activity: Context,
        targetView: View,
        threshold: Int
    ): Boolean {
        val screenHeight = getScreenHeight(activity)
        val positionTargetAxisY = getAxisYpositionOfViewOnScreen(targetView)
        return (screenHeight - threshold) < positionTargetAxisY
    }

    fun isViewLocatedAtTopOfTheScreen(
        activity: Context,
        targetView: View,
        threshold: Int
    ): Boolean {
        val screenHeight = getScreenHeight(activity)
        val positionTargetAxisY = getAxisYpositionOfViewOnScreen(targetView)
        return (positionTargetAxisY - threshold) < 0
    }

    fun isViewLocatedAtHalfLeftOfTheScreen(activity: Activity, targetView: View): Boolean {
        val screenWidth = getScreenWidth(activity)
        val positionTargetAxisX = getAxisXpositionOfViewOnScreen(targetView)
        return screenWidth / 2 > positionTargetAxisX
    }

    fun getScaleAnimation(offset: Int, duration: Int): Animation {
        val anim = ScaleAnimation(
            0f, 1f, // Start and end values for the X axis scaling
            0f, 1f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = true
        anim.startOffset = offset.toLong()
        anim.duration = duration.toLong()
        return anim
    }

    fun getFadeInAnimation(offset: Int, duration: Int): Animation {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.startOffset = offset.toLong()
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = duration.toLong()
        return fadeIn
    }

    fun setBouncingAnimation(view: View, offset: Int, duration: Int): View {

        val objAnim = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.05f),
            PropertyValuesHolder.ofFloat("scaleY", 1.05f)
        )
        objAnim.duration = duration.toLong()
        objAnim.startDelay = offset.toLong()
        objAnim.repeatCount = ObjectAnimator.INFINITE
        objAnim.repeatMode = ObjectAnimator.REVERSE
        objAnim.start()
        return view
    }

    fun setAnimationToView(view: View, animation: Animation): View {
        view.startAnimation(animation)
        return view
    }
}

val View.locationOnScreen: Point
    get() = IntArray(2).let {
        getLocationOnScreen(it)
        Point(it[0], it[1])

    }