package co.yap.widgets.spinneradapter.searchable

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.EditText
import android.widget.TextView


    fun EditText.setCursorColor( color: Int) {
        try {
            val drawableResField =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            drawableResField.isAccessible = true
            val drawable = getDrawable(
                this.context,
                drawableResField.getInt(this)
            )
                ?: return
            //BlendModeColorFilter(color,BlendMode.SRC_IN)
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            val drawableFieldOwner: Any
            val drawableFieldClass: Class<*>
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                drawableFieldOwner = this
                drawableFieldClass = TextView::class.java
            } else {
                val editorField =
                    TextView::class.java.getDeclaredField("mEditor")
                editorField.isAccessible = true
                drawableFieldOwner = editorField[this]
                drawableFieldClass = drawableFieldOwner.javaClass
            }
            val drawableField =
                drawableFieldClass.getDeclaredField("mCursorDrawable")
            drawableField.isAccessible = true
            drawableField[drawableFieldOwner] = arrayOf(drawable, drawable)
        } catch (ignored: Exception) {
        }
    }

    private fun getDrawable(context: Context, id: Int)=context.getDrawable(id)
