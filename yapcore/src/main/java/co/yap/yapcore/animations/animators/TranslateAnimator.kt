package co.yap.yapcore.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import co.yap.yapcore.animations.Animator
import co.yap.yapcore.animations.IAnimator

class TranslateAnimator(
    private val direction: String,
    val from: Float,
    val to: Float,
    private val translateInterpolator: Interpolator? = DecelerateInterpolator()
) : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet =
        runTogether(ObjectAnimator.ofFloat(view, direction, from, to)).apply {
            this.duration = duration!!
            this.interpolator = translateInterpolator
        }
}