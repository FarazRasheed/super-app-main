package com.digitify.identityscanner.camera.controls


import androidx.annotation.Keep
import com.digitify.identityscanner.camera.CameraOptions
import com.digitify.identityscanner.camera.CameraView

/**
 * White balance values control the white balance settings.
 *
 * @see CameraView.setWhiteBalance
 */
@Keep
enum class WhiteBalance(private val value: Int) : Control {

    /**
     * Automatic white balance selection (AWB).
     * This is not guaranteed to be supported.
     *
     * @see CameraOptions.getSupportedWhiteBalance
     */
    AUTO(0),

    /**
     * White balance appropriate for incandescent light.
     * This is not guaranteed to be supported.
     *
     * @see CameraOptions.getSupportedWhiteBalance
     */
    INCANDESCENT(1),

    /**
     * White balance appropriate for fluorescent light.
     * This is not guaranteed to be supported.
     *
     * @see CameraOptions.getSupportedWhiteBalance
     */
    FLUORESCENT(2),

    /**
     * White balance appropriate for daylight captures.
     * This is not guaranteed to be supported.
     *
     * @see CameraOptions.getSupportedWhiteBalance
     */
    DAYLIGHT(3),

    /**
     * White balance appropriate for pictures in cloudy conditions.
     * This is not guaranteed to be supported.
     *
     * @see CameraOptions.getSupportedWhiteBalance
     */
    CLOUDY(4);

    internal fun value(): Int {
        return value
    }

    companion object {

        internal val DEFAULT = AUTO

        internal fun fromValue(value: Int): WhiteBalance {
            val list = WhiteBalance.values()
            for (action in list) {
                if (action.value() == value) {
                    return action
                }
            }
            return DEFAULT
        }
    }
}