package co.yap.widgets.vectorfont

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import co.yap.yapcore.R


class FontTextView : AppCompatTextView {
    private var isBrandingIcon: Boolean = false
    private var isSolidIcon: Boolean = false

    constructor(context: Context) : super(context)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.FontTextView,
            0, 0
        )
        isSolidIcon = a.getBoolean(R.styleable.FontTextView_solid_icon, false)
        isBrandingIcon = a.getBoolean(R.styleable.FontTextView_brand_icon, false)
        init()
    }

    private fun init() {
        if (isBrandingIcon)
            typeface = FontCache[context, FontCache.FA_FONT_BRANDS]
        else if (isSolidIcon)
            typeface = FontCache[context, FontCache.FA_FONT_SOLID]
        else
            typeface = FontCache[context, FontCache.FA_FONT_REGULAR]
    }
}