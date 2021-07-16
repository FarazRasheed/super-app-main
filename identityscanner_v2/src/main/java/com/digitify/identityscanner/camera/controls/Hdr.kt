package com.digitify.identityscanner.camera.controls

import androidx.annotation.Keep


/**
 * Hdr values indicate whether to use high dynamic range techniques when capturing pictures.
 *
 * @see CameraView.setHdr
 */
@Keep
enum class Hdr(private val value: Int) : Control {

    /**
     * No HDR.
     */
    OFF(0),

    /**
     * Using HDR.
     */
    ON(1);

    internal fun value(): Int {
        return value
    }

    companion object {

        internal val DEFAULT = OFF

        internal fun fromValue(value: Int): Hdr {
            val list = Hdr.values()
            for (action in list) {
                if (action.value() == value) {
                    return action
                }
            }
            return DEFAULT
        }
    }
}
