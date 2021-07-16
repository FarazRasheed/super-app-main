package com.digitify.identityscanner.camera.controls


import androidx.annotation.Keep
import com.digitify.identityscanner.camera.CameraView

/**
 * The preview engine to be used.
 *
 * @see CameraView.setPreview
 */
@Keep
enum class Preview(private val value: Int) : Control {

    /**
     * Preview engine based on [android.view.SurfaceView].
     * Not recommended.
     */
    SURFACE(0),

    /**
     * Preview engine based on [android.view.TextureView].
     * Stable, but does not support all features (like video snapshots,
     * or picture snapshot while taking videos).
     */
    TEXTURE(1),

    /**
     * Preview engine based on [android.opengl.GLSurfaceView].
     * This is the best engine available. Supports video snapshots,
     * supports picture snapshots while taking videos, supports
     * watermarks and overlays, supports real-time filters.
     */
    GL_SURFACE(2);

    internal fun value(): Int {
        return value
    }

    companion object {

        internal val DEFAULT = GL_SURFACE

        internal fun fromValue(value: Int): Preview {
            val list = Preview.values()
            for (action in list) {
                if (action.value() == value) {
                    return action
                }
            }
            return DEFAULT
        }
    }
}
