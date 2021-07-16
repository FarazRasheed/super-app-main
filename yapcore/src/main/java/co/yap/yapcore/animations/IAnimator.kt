package co.yap.yapcore.animations

import android.animation.AnimatorSet
import android.view.View

interface IAnimator {
    fun with(view: View, duration: Long? = 500): AnimatorSet
}