package co.yap.yapcore.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import co.yap.yapcore.animations.Animator
import co.yap.yapcore.animations.IAnimator


class FadeOutAnimator : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet =
        runTogether(ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)).apply {
            this.duration = duration!!
        }
}