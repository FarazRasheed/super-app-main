package co.yap.yapcore.animations.interpolator

import kotlin.math.cos
import kotlin.math.pow

class BounceInterpolator(amplitude: Double, frequency: Double) : android.view.animation.Interpolator {

    private var mAmplitude = 1.0
    private var mFrequency = 10.0

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }

    override fun getInterpolation(time: Float): Float {
        return (-1.0 * Math.E.pow(-time / mAmplitude) *
                cos(mFrequency * time) + 1).toFloat()
    }
}