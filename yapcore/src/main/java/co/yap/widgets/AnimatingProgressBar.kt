package co.yap.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar

class AnimatingProgressBar : ProgressBar {

    private var animator: ValueAnimator? = null
    private var animatorSecondary: ValueAnimator? = null
    var isAnimate = true

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    @Synchronized
    override fun setProgress(progress: Int) {
        if (!isAnimate) {
            super.setProgress(progress)
            return
        }
        if (animator != null)
            animator!!.cancel()
        if (animator == null) {
            animator = ValueAnimator.ofInt(getProgress(), progress)
            animator!!.interpolator = DEFAULT_INTERPOLATER
            animator!!.addUpdateListener { animation -> super@AnimatingProgressBar.setProgress(animation.animatedValue as Int) }
        } else
            animator!!.setIntValues(getProgress(), progress)
        animator!!.start()

    }

    @Synchronized
    override fun setSecondaryProgress(secondaryProgress: Int) {
        if (!isAnimate) {
            super.setSecondaryProgress(secondaryProgress)
            return
        }
        if (animatorSecondary != null)
            animatorSecondary!!.cancel()
        if (animatorSecondary == null) {
            animatorSecondary = ValueAnimator.ofInt(progress, secondaryProgress)
            animatorSecondary!!.interpolator = DEFAULT_INTERPOLATER
            animatorSecondary!!.addUpdateListener { animation ->
                super@AnimatingProgressBar.setSecondaryProgress(
                    animation
                        .animatedValue as Int
                )
            }
        } else
            animatorSecondary!!.setIntValues(progress, secondaryProgress)
        animatorSecondary!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animator != null)
            animator!!.cancel()
        if (animatorSecondary != null)
            animatorSecondary!!.cancel()
    }

    companion object {

        private val DEFAULT_INTERPOLATER = AccelerateDecelerateInterpolator()
    }

}