package com.digitify.identityscanner.camera.controls


import androidx.annotation.Keep
import com.digitify.identityscanner.camera.CameraView

/**
 * Type of the session to be opened or to move to.
 * Session modes have influence over the capture and preview size, ability to shoot pictures,
 * focus modes, runtime permissions needed.
 *
 * @see CameraView.setMode
 */
@Keep
enum class Mode(private val value: Int) : Control {

    /**
     * Session used to capture pictures.
     *
     * - [CameraView.takeVideo] will throw an exception
     * - Only the camera permission is requested
     * - Capture size is chosen according to the current picture size selector
     */
    PICTURE(0);

    internal fun value(): Int {
        return value
    }

    companion object {

        /**
         * Session used to capture videos.
         *
         * - [CameraView.takePicture] will throw an exception
         * - Camera and audio record permissions are requested
         * - Capture size is chosen according to the current video size selector
         */
        //    VIDEO(1);

        internal val DEFAULT = PICTURE

        internal fun fromValue(value: Int): Mode {
            val list = Mode.values()
            for (action in list) {
                if (action.value() == value) {
                    return action
                }
            }
            return DEFAULT
        }
    }
}
