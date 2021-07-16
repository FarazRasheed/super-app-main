package com.digitify.identityscanner.camera.controls


import androidx.annotation.Keep
import com.digitify.identityscanner.camera.CameraView

/**
 * Grid values can be used to draw grid lines over the camera preview.
 *
 * @see CameraView.setGrid
 */
@Keep
enum class Grid(private val value: Int) : Control {


    /**
     * No grid is drawn.
     */
    OFF(0),

    /**
     * Draws a regular, 3x3 grid.
     */
    DRAW_3X3(1),

    /**
     * Draws a regular, 4x4 grid.
     */
    DRAW_4X4(2),

    /**
     * Draws a grid respecting the 'phi' constant proportions,
     * often referred as to the golden ratio.
     */
    DRAW_PHI(3);

    internal fun value(): Int {
        return value
    }

    companion object {

        internal val DEFAULT = OFF

        internal fun fromValue(value: Int): Grid {
            val list = Grid.values()
            for (action in list) {
                if (action.value() == value) {
                    return action
                }
            }
            return DEFAULT
        }
    }
}