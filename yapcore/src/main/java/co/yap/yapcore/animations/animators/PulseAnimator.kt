package co.yap.yapcore.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import co.yap.yapcore.animations.Animator
import co.yap.yapcore.animations.IAnimator
import co.yap.yapcore.helpers.AnimationUtils

class PulseAnimator : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet = AnimationUtils.runTogether(
        ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 0.95f, 1f),
        ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 0.95f, 1f)
    ).apply { this.duration = duration!! }
}