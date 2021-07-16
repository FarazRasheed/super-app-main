package co.yap.yapcore.helpers

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

object ViewAnimationUtils {

    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val targtetHeight = v.getMeasuredHeight()

        v.getLayoutParams().height = 0
        v.setVisibility(View.VISIBLE)
        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.getLayoutParams().height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targtetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.setDuration(((targtetHeight / v.getContext().getResources().getDisplayMetrics().density) as Int).toLong())
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.getMeasuredHeight()

        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.setVisibility(View.GONE)
                } else {
                    v.getLayoutParams().height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

           override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.setDuration(((initialHeight / v.getContext().getResources().getDisplayMetrics().density) as Int).toLong())
        v.startAnimation(a)
    }
}