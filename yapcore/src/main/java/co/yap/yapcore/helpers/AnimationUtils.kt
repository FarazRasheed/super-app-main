package co.yap.yapcore.helpers

import android.animation.*
import android.content.Context
import android.view.View
import android.view.animation.*
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import co.yap.yapcore.animations.animators.*

object AnimationUtils {

    /**
     * Run a set of Animators in Parallel
     */
    fun runTogether(vararg animator: Animator): AnimatorSet =
        AnimatorSet().apply { playTogether(*animator) }

    /**
     * Run a set of Animators in sequence
     */
    fun runSequentially(vararg animator: Animator): AnimatorSet =
        AnimatorSet().apply { playSequentially(*animator) }


    /**
     * Load Animator from resource animator and apply on the view
     */
    fun loadAnimator(context: Context, @AnimatorRes animatorResId: Int, view: View): AnimatorSet {
        return (AnimatorInflater.loadAnimator(context, animatorResId) as AnimatorSet)
            .apply {
                setTarget(view)
            }
    }

    fun loadAnimation(context: Context, @AnimRes resId: Int): Animation =
        AnimationUtils.loadAnimation(context, resId)


    fun fadeIn(view: View, duration: Long? = 500): AnimatorSet =
        FadeInAnimator().with(view, duration)

    fun fadeOut(view: View, duration: Long? = 500): AnimatorSet =
        FadeOutAnimator().with(view, duration)

    fun scale(
        view: View,
        duration: Long? = 500,
        from: Float,
        to: Float,
        interpolator: Interpolator? = LinearInterpolator()
    ): AnimatorSet =
        ScaleAnimator(from, to, interpolator).with(view, duration)

    fun pulse(view: View, duration: Long? = 500): AnimatorSet = PulseAnimator().with(view, duration)

    fun bounce(view: View, duration: Long? = 500, from: Float, to: Float): AnimatorSet =
        scale(view, duration, from, to, OvershootInterpolator())

    fun slideVertical(
        view: View,
        duration: Long? = 500,
        from: Float,
        to: Float,
        interpolator: Interpolator? = DecelerateInterpolator()
    ): AnimatorSet = TranslateAnimator("y", from, to, interpolator).with(view, duration)

    fun slideHorizontal(
        view: View,
        duration: Long? = 500,
        from: Float,
        to: Float,
        interpolator: Interpolator? = DecelerateInterpolator()
    ): AnimatorSet = TranslateAnimator("x", from, to, interpolator).with(view, duration)

    /**
     * Translate and FadeIn animation running in parallel on the view
     */
    fun jumpInAnimation(
        view: View,
        duration: Long? = 500,
        from: Float? = view.y + 300,
        to: Float? = view.y
    ): AnimatorSet =
        runTogether(
            fadeIn(view, 200),
            slideVertical(view, duration!!, from!!, to!!, OvershootInterpolator())
        )

    /**
     * Bounce and FadeIn animation running in parallel on the view
     */
    fun outOfTheBoxAnimation(view: View): AnimatorSet =
        runTogether(fadeIn(view, 150), bounce(view, 300, 0.5f, 1f))

    fun valueCounter(initial: Int, final: Int, duration: Long? = 500): ValueAnimator =
        ValueAnimator.ofInt(initial, final).apply {
            this.duration = duration ?: 0
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