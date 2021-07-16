package co.yap.yapcore.helpers.spannables

import android.content.Context
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

class ResSpans(val context: Context) : Iterable<Any> {
    val spans = ArrayList<Any>()

    override fun iterator() = spans.iterator()

    fun appearance(@StyleRes id: Int) =
        spans.add(TextAppearanceSpan(context, id))

    fun size(@DimenRes id: Int) =
        spans.add(AbsoluteSizeSpan(context.resources.getDimension(id).toInt()))

    fun color(@ColorRes id: Int) =
        spans.add(ForegroundColorSpan(ContextCompat.getColor(context, id)))

    fun icon(@DrawableRes id: Int, size: Int) =
        spans.add(ImageSpan(AppCompatResources.getDrawable(context, id)!!.apply {
            setBounds(0, 0, size, size)
        }))

    fun sansSerifMedium() = typeface("sans-serif-medium")
    fun sansSerifRegular() = typeface("sans-serif-regular")

    fun typeface(family: String) = spans.add(TypefaceSpan(family))
    fun typeface(style: Int) = spans.add(StyleSpan(style))

    fun click(action: () -> Unit) = spans.add(clickableSpan(action))

    fun custom(span: Any) = spans.add(span)
}

fun clickableSpan(action: () -> Unit) = object : ClickableSpan() {
    override fun onClick(view: View) = action()
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}
