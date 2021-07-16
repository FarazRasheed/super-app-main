package co.yap.widgets.currency

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.TextViewCompat
import co.yap.yapcore.R
import co.yap.yapcore.helpers.DecimalDigitsInputFilter
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.Utils.dpToFloat
import co.yap.yapcore.helpers.extentions.dip2px
import co.yap.yapcore.helpers.extentions.getColors
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

@Deprecated("Use EasyMoneyEditText")
class CoreEditText : AppCompatEditText {

    private var mBackgroundColor: Int = 0
    private var mCornerRadius: Float = dpToFloat(context, 10f)
    private var mStrokeWidth = dpToFloat(context, 0f)
    private var customWidth: Int = 0
    private var customHeight: Int = 0

    var decimals: Int = Utils.getConfiguredDecimals(SessionManager.getDefaultCurrency())
    var units: Int = resources.getInteger(R.integer.unitsCount)

    var currency: String? =SessionManager.getDefaultCurrency()
        set(value) {
            field = value
            decimals = Utils.getConfiguredDecimals(value ?: SessionManager.getDefaultCurrency())
            filters =
                arrayOf(InputFilter.LengthFilter(units), DecimalDigitsInputFilter(decimals))
            hint = "0".toFormattedCurrency(showCurrency = false, currency = value ?: SessionManager.getDefaultCurrency())
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CoreEditText)
        mBackgroundColor =
            a.getColor(
                R.styleable.CoreEditText_ce_setBackgroundColor,
                context.getColors(R.color.greySoft)
            )
        customWidth = a.getDimension(
            R.styleable.CoreEditText_ce_custom_width, context.dip2px(
                0
            ).toFloat()
        ).toInt()
        customHeight = a.getDimension(
            R.styleable.CoreEditText_ce_custom_height, context.dip2px(
                0
            ).toFloat()
        ).toInt()

        filters =
            arrayOf(InputFilter.LengthFilter(units), DecimalDigitsInputFilter(decimals))
        hint = "0".toFormattedCurrency(showCurrency = false, currency = currency ?: SessionManager.getDefaultCurrency())
        background = getShapeBackground()
        //setCursorColor(context.getColors(R.color.colorPrimary))
        gravity = Gravity.CENTER
        @RequiresApi(Build.VERSION_CODES.O)
        importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        setSingleLine()
        maxLines = 1
        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setEditTextDimension()
    }


    private fun setEditTextDimension() {
        val dimensions = Utils.getDimensionsByPercentage(context, 50, 8)
        val params = layoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        layoutParams = params
    }

    private fun getShapeBackground(@ColorInt color: Int = context.getColors(R.color.colorPrimary)): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = mCornerRadius
        shape.setColor(mBackgroundColor)
        shape.setStroke(mStrokeWidth.toInt(), color)
        return shape
    }

    public override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
    }
}