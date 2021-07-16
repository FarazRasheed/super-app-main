package co.yap.yapcore.animations

import android.animation.Animator
import android.animation.AnimatorSet

abstract class Animator : IAnimator {
    /**
     * Run a set of Animators in Parallel
     */
    fun runTogether(vararg animator: Animator): AnimatorSet = AnimatorSet().apply { playTogether(*animator) }

    /**
     * Run a set of Animators in sequence
     */
    fun runSequentially(vararg animator: Animator): AnimatorSet = AnimatorSet().apply { playSequentially(*animator) }
}