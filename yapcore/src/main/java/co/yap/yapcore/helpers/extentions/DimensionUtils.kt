package co.yap.yapcore.helpers.extentions

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

const val LDPI: Int = DisplayMetrics.DENSITY_LOW
const val MDPI: Int = DisplayMetrics.DENSITY_MEDIUM
const val HDPI: Int = DisplayMetrics.DENSITY_HIGH

const val TVDPI: Int = DisplayMetrics.DENSITY_TV
const val XHDPI: Int = DisplayMetrics.DENSITY_XHIGH
const val XXHDPI: Int = DisplayMetrics.DENSITY_XXHIGH
const val XXXHDPI: Int = DisplayMetrics.DENSITY_XXXHIGH

//returns dip(dp) dimension value in pixels
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

//return sp dimension value in pixels
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()


/**
 * convert dip to px
 *
 * @param[value] to convert
 * @return calculated dip
 */
fun Context.dip2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/**
 * convert dip to px
 *
 * @param[value] to convert
 * @return calculated dip
 * @since 1.0.1
 */
fun Context.dip2px(value: Float): Float = (value * resources.displayMetrics.density)

/**
 * convert sp to px
 *
 * @param[value] to convert
 * @return calculated sp
 */
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()


/**
 * convert sp to px
 *
 * @param[value] to convert
 * @return calculated sp
 */
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()


/**
 * convert px to dip
 *
 * @param[px] to convert
 * @return calculated dip
 */
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density

/**
 * convert px to sp
 *
 * @param[px] to convert
 * @return calculated sp
 */
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

/**
 * get pixel size from DimenRes
 *
 * @param[resource] dimen res to convert
 * @return proper pixel size
 */
fun Context.dimen(resource: Int): Int = resources.getDimensionPixelSize(resource)
/* Functions for Conversions */

fun Fragment.dimen(resource: Int) = context?.dimen(resource)

/** gets display size as a point. */
internal fun Context.displaySize(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

/** gets a color from the resource. */
internal fun Context.contextColor(resource: Int): Int {
    return ContextCompat.getColor(this, resource)
}

/** gets a drawable from the resource. */
internal fun Context.contextDrawable(resource: Int): Drawable? {
    return ContextCompat.getDrawable(this, resource)
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}
fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}
