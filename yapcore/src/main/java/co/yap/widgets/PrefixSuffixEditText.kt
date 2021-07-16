package co.yap.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import co.yap.yapcore.R
import co.yap.yapcore.helpers.Utils.getDefaultCountryCode
import co.yap.yapcore.helpers.getCountryCodeForRegion
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.*


class PrefixSuffixEditText : AppCompatEditText {
    private val DEFAULTCOLOR = Color.parseColor("#9391b1")
    private var mBackgroundColor: Int = 0
    private var clearIconTint: Int = 0
    private var hideShowIconTint: Int = 0
    private var prefixTextColor: Int = 0
    private var cPadding: Int = 0
    private var cPaddingLeft: Int = 0
    private var cPaddingTop: Int = 0
    private var cPaddingRight: Int = 0
    private var cPaddingBottom: Int = 0
    private var mCornerRadius: Float = 0.toFloat()
    private var mStrokeWidth = 1f
    private var mOriginalLeftPadding = -1f
    private var isClearIconVisible: Boolean = false
    private var isPassword = false
    private var isShowingPassword = false
    private var imgCloseButton: Drawable? = null
    private var drawableEnd: Drawable? = null
    private var mDrawableWidth: Int = 0
    private var mDrawableHeight: Int = 0
    private var pseSpace: Int = 0
    private var mPrefix: String? = null
    private var prefixBitmap: Bitmap? = null
    private var showPrefixDrawable: Boolean = true
    private var showHint: Boolean = false
    // private val textFormatter = PhoneNumberFormatter(Locale.getDefault().country)
    var prefix: String?
        get() = this.mPrefix
        set(prefix) {
            this.mPrefix = prefix
            if (mPrefix?.isNotBlank()!!)
                mask(mPrefix)
            // mOriginalLeftPadding = -1f
            // textFormatter.countryCode = "CZ"
            calculatePrefixPadding()
//            calculatePrefix()
            invalidate()
        }

    var prefixDrawable: Drawable? = null
        set(value) {
            field = value
            if (value != null) {
                prefixBitmap = drawableToBitmap(prefixDrawable)
                calculatePrefix()
                invalidate()
            }
        }
    private var mPrefixDrawable: Drawable? = null

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

    private fun init(context: Context, attrs: AttributeSet?) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.PrefixSuffixEditText)
        imgCloseButton =
            ContextCompat.getDrawable(getContext(), android.R.drawable.ic_menu_close_clear_cancel)
        cPadding = a.getDimensionPixelSize(R.styleable.PrefixSuffixEditText_android_padding, -1)
        cPaddingLeft = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_android_paddingLeft,
            DEFAULT_PADDING
        )
        cPaddingTop = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_android_paddingTop,
            DEFAULT_PADDING
        )
        mDrawableWidth = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_pse_compoundDrawableWidth,
            -1
        )
        mDrawableHeight = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_pse_compoundDrawableHeight,
            -1
        )
        pseSpace = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_pse_space,
            10
        )
        cPaddingRight = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_android_paddingRight,
            DEFAULT_PADDING
        )

        cPaddingBottom = a.getDimensionPixelSize(
            R.styleable.PrefixSuffixEditText_android_paddingBottom,
            DEFAULT_PADDING
        )
        showPrefixDrawable =
            a.getBoolean(R.styleable.PrefixSuffixEditText_pse_showPrefixDrawable, true)
        showHint =
            a.getBoolean(R.styleable.PrefixSuffixEditText_pse_showHint, false)
        isClearIconVisible =
            a.getBoolean(R.styleable.PrefixSuffixEditText_pse_setClearIconVisible, false)
        val isBorderView = a.getBoolean(R.styleable.PrefixSuffixEditText_pse_setBorderView, false)
        val mNormalColor =
            a.getColor(R.styleable.PrefixSuffixEditText_pse_setBorderColor, DEFAULTCOLOR)
        val cursorColor = a.getColor(R.styleable.PrefixSuffixEditText_pse_setCursorColor, 0)
        mBackgroundColor =
            a.getColor(R.styleable.PrefixSuffixEditText_pse_setBackgroundColor, Color.TRANSPARENT)
        mStrokeWidth =
            a.getDimension(R.styleable.PrefixSuffixEditText_pse_setStrokeWidth, mStrokeWidth)
        hideShowIconTint =
            a.getColor(R.styleable.PrefixSuffixEditText_pse_hideShowPasswordIconTint, DEFAULTCOLOR)
        clearIconTint = a.getColor(R.styleable.PrefixSuffixEditText_pse_clearIconTint, DEFAULTCOLOR)
        mPrefix = a.getString(R.styleable.PrefixSuffixEditText_pse_setPrefix)
        prefixTextColor =
            a.getColor(R.styleable.PrefixSuffixEditText_pse_setPrefixTextColor, DEFAULTCOLOR)
        mCornerRadius = a.getDimension(R.styleable.PrefixSuffixEditText_pse_setCornerRadius, 1f)
        mPrefixDrawable = a.getDrawable(R.styleable.PrefixSuffixEditText_pse_setPrefixDrawable)

//        if (isBorderView) {
//            setBackGroundOfLayout(getShapeBackground(mNormalColor))
//            setCursorColor(cursorColor)
//        } else {
//            padding(false)
//        }
        if (inputType == TYPE_TEXT_VARIATION_PASSWORD || inputType == TYPE_NUMBER_VARIATION_PASSWORD) {
            isPassword = true
            this.maxLines = 1
        } else if (inputType == EditorInfo.TYPE_CLASS_PHONE) {
            this.keyListener = DigitsKeyListener.getInstance("0123456789")
        }
        if (!isPassword && isClearIconVisible) {
            handleClearButton()
        }

        prefixDrawable = mPrefixDrawable
        if (mPrefix != null && mPrefix!!.length > 0) {
            calculatePrefix()
        }
        if(!showPrefixDrawable) prefix = mPrefix

        if (isPassword)
            if (!TextUtils.isEmpty(text)) {
                showPasswordVisibilityIndicator(true)
            } else {
                showPasswordVisibilityIndicator(false)
            }
        setOnTouchListener(OnTouchListener { view, event ->
            val editText = this@PrefixSuffixEditText
            if (editText.compoundDrawables[2] == null)
                return@OnTouchListener false
            if (event.action != MotionEvent.ACTION_UP)
                return@OnTouchListener false
            if (isPassword) {
                val width = if (drawableEnd == null) 0 else drawableEnd!!.intrinsicWidth
                if (event.x > editText.width - editText.paddingRight - width) {
                    togglePasswordVisibility()
                    event.action = MotionEvent.ACTION_CANCEL
                }
            } else if (isClearIconVisible) {
                val width = if (imgCloseButton == null) 0 else imgCloseButton!!.intrinsicWidth
                if (event.x > editText.width - editText.paddingRight - width) {
                    editText.setText("")
                    this@PrefixSuffixEditText.handleClearButton()
                }
            }
            false
        })
        //textFormatter.countryCode = "PK"
        ///addTextChangedListener(textFormatter)
        a.recycle()
        // mask("PK")
//        val drawables = compoundDrawables
//        /// icon = drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.flag_ad)!!)
//
//        setCompoundDrawables(null, null, null, null)
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mPrefix != null)
            calculatePrefix()
    }


    public fun setCursorColor(@ColorInt color: Int) = try {
        var c = color
        if (c == 0) c = getThemeAccentColor()
        // Get the cursor resource id
        var field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        field.isAccessible = true
        val drawableResId = field.getInt(this)

        // Get the editor
        field = TextView::class.java.getDeclaredField("mEditor")
        field.isAccessible = true
        val editor = field.get(this)

        // Get the drawable and set a color filter
        val drawable = ContextCompat.getDrawable(this.context, drawableResId)
        drawable?.setColorFilter(c, PorterDuff.Mode.SRC_IN)
        val drawables = arrayOf<Drawable>(drawable!!, drawable)

        // Set the drawables
        field = editor.javaClass.getDeclaredField("mCursorDrawable")
        field.isAccessible = true
        field.set(editor, drawables)
    } catch (ignored: Exception) {
    }

    /**
     * This method is used to set the rectangle box on EditText
     */
//    private fun setBackGroundOfLayout(shape: Drawable?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            background = shape
//        } else {
//            setBackgroundDrawable(shape)
//        }
//        padding(true)
//    }
//
//    private fun padding(isRound: Boolean) {
//        val extraPadding: Int
//        val extraPad: Int
//        if (isRound) {
//            extraPadding = 5
//            extraPad = 0
//        } else {
//            extraPad = 5
//            extraPadding = 0
//        }
//        if (cPadding != -1) {
//            super.setPadding(cPadding + extraPadding, cPadding, cPadding, cPadding + extraPad)
//        } else {
//            super.setPadding(
//                cPaddingLeft + extraPadding,
//                cPaddingTop,
//                cPaddingRight,
//                cPaddingBottom + extraPad
//            )
//        }
//    }


    /**
     * This method is used to draw the rectangle border view with color
     */
    @SuppressLint("WrongConstant")
    private fun getShapeBackground(@ColorInt color: Int): Drawable {

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = mCornerRadius
        shape.setColor(mBackgroundColor)
        shape.setStroke(mStrokeWidth.toInt(), color)
        return shape
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPrefix != null) {
            val prefix = mPrefix
            var myPaint: Paint? = null
            if (prefixTextColor != 0) {
                myPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                myPaint.color = prefixTextColor
                myPaint.textAlign = Paint.Align.LEFT
                myPaint.textSize = textSize
            }
            //val icon  = BitmapFactory.decodeResource(context.resources,
            //R.drawable.flag_ad)

//            val paint1: Paint? = Paint()
            // canvas.drawBitmap(icon , Rect(10,10,10,10),myPaint)

            if (prefixBitmap == null || !showPrefixDrawable) {
                canvas.drawText(
                    prefix!!, mOriginalLeftPadding, getLineBounds(0, null).toFloat(), myPaint
                        ?: paint
                )
            } else {
//                canvas.drawBitmap(
//                    prefixBitmap!!,
//                    mOriginalLeftPadding,
//                    (height / 2 - prefixBitmap?.height!! / 2).toFloat(),
//                    myPaint
//                        ?: paint
//                )
                canvas.drawBitmap(
                    prefixBitmap!!,
                    mOriginalLeftPadding,
                    (((height - prefixBitmap?.height!!) / 2) - (paddingBottom / 2 - paddingTop)).toFloat(),
                    myPaint
                        ?: paint
                )
                canvas.drawText(
                    prefix!!,
                    prefixBitmap?.width?.plus(mOriginalLeftPadding)?.plus(pseSpace / 2)!!,
                    getLineBounds(0, null).toFloat(),
                    myPaint
                        ?: paint
                )
            }
        }
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable: BitmapDrawable = drawable as BitmapDrawable
            if (bitmapDrawable.bitmap != null) {
                val b = Bitmap.createScaledBitmap(
                    bitmapDrawable.bitmap,
                    mDrawableWidth,
                    mDrawableHeight,
                    false
                )
                return b
            }
        }
        bitmap = if (drawable?.intrinsicWidth!! <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                mDrawableWidth,
                mDrawableHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap!!)
        drawable.setBounds(0, 0, canvas.width, canvas.height);
        drawable.draw(canvas)
        return bitmap
    }

    @SuppressLint("NewApi")
    private fun handleClearButton() {
        if (isClearIconVisible) {
            DrawableCompat.setTint(imgCloseButton!!, clearIconTint)
            imgCloseButton!!.setBounds(0, 0, 43, 43)
            if (Objects.requireNonNull<Editable>(this.text).isEmpty()) {
                this.setCompoundDrawables(
                    this.compoundDrawables[0],
                    this.compoundDrawables[1],
                    null,
                    this.compoundDrawables[3]
                )
            } else {
                this.setCompoundDrawables(
                    this.compoundDrawables[0],
                    this.compoundDrawables[1],
                    imgCloseButton,
                    this.compoundDrawables[3]
                )
            }
        }
    }


//    public override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
//        try {
//            if (isPassword) {
//                if (s.isNotEmpty()) {
//                    showPasswordVisibilityIndicator(true)
//                } else {
//                    isShowingPassword = false
//                    maskPassword()
//                    showPasswordVisibilityIndicator(false)
//                }
//            } else if (isClearIconVisible)
//                this@PrefixSuffixEditText.handleClearButton()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

    private fun showPasswordVisibilityIndicator(show: Boolean) {
        if (show) {
            val original = if (isShowingPassword)
                ContextCompat.getDrawable(context, R.drawable.passcode_empty_circle)
            else
                ContextCompat.getDrawable(context, R.drawable.passcode_empty_circle)
            if (original != null) {
                original.mutate()
                DrawableCompat.setTint(original, hideShowIconTint)
                original.setBounds(0, 0, 43, 43)
                drawableEnd = original
                this.setCompoundDrawables(
                    this.compoundDrawables[0],
                    this.compoundDrawables[1],
                    original,
                    this.compoundDrawables[3]
                )
            }
        } else {
            this.setCompoundDrawables(
                this.compoundDrawables[0],
                this.compoundDrawables[1],
                null,
                this.compoundDrawables[3]
            )
        }
    }

    //make it visible
    private fun unmaskPassword() {
        transformationMethod = null
    }

    //hide it
    private fun maskPassword() {
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun getThemeAccentColor(): Int {
        val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.R.attr.colorAccent
        } else {
            context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        }
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }

    private fun togglePasswordVisibility() {
        // Store the selection
        val selectionStart = this.selectionStart
        val selectionEnd = this.selectionEnd
        // Set transformation method to show/hide password
        if (isShowingPassword) {
            maskPassword()
        } else {
            unmaskPassword()
        }
        // Restore selection
        this.setSelection(selectionStart, selectionEnd)
        // Toggle flag and show indicator
        isShowingPassword = !isShowingPassword
        showPasswordVisibilityIndicator(true)
    }


    private fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            val prefix = mPrefix
            val widths = FloatArray(prefix!!.length)
            paint.getTextWidths(prefix, widths)
            var textWidth = 0f
            for (w in widths) {
                textWidth += w
            }
            mOriginalLeftPadding = compoundPaddingLeft.toFloat()
            if (prefixBitmap != null && showPrefixDrawable) {
                setPadding(
                    (prefixBitmap?.width!! + textWidth + mOriginalLeftPadding).toInt() + pseSpace,
                    paddingRight, paddingTop,
                    paddingBottom
                )
            } else {
                setPadding(
                    (textWidth + mOriginalLeftPadding).toInt() + pseSpace,
                    paddingRight, paddingTop,
                    paddingBottom
                )
            }
        }
    }

    private fun calculatePrefixPadding() {
        // if (mOriginalLeftPadding == -1f) {
        val prefix = mPrefix
        val widths = FloatArray(prefix!!.length)
        paint.getTextWidths(prefix, widths)
        var textWidth = 0f
        for (w in widths) {
            textWidth += w
        }

        if (prefixBitmap != null && showPrefixDrawable) {
            setPadding(
                (prefixBitmap?.width!! + textWidth + mOriginalLeftPadding).toInt() + pseSpace,
                paddingRight, paddingTop,
                paddingBottom
            )
        } else {
            setPadding(
                (textWidth + mOriginalLeftPadding).toInt() + pseSpace,
                paddingRight, paddingTop,
                paddingBottom
            )
        }
        // }
    }

    public fun setPrefixTextColor(prefixTextColor: Int) {
        this.prefixTextColor = prefixTextColor
        invalidate()
    }

    private fun scale(): Drawable? {
        if (mDrawableHeight > 0 || mDrawableWidth > 0) {
            // for (drawable in drawables) {
//                if (drawable == null) {
//                    continue
//                }

            val realBounds =
                Rect(0, 0, mPrefixDrawable?.intrinsicWidth!!, mPrefixDrawable?.intrinsicHeight!!)
            var actualDrawableWidth = realBounds.width().toFloat()
            var actualDrawableHeight = realBounds.height().toFloat()
            val actualDrawableRatio = actualDrawableHeight / actualDrawableWidth

            val scale: Float
            // check if both width and height defined then adjust drawable size according to the ratio
            if (mDrawableHeight > 0 && mDrawableWidth > 0) {
                val placeholderRatio = mDrawableHeight / mDrawableWidth.toFloat()
                if (placeholderRatio > actualDrawableRatio) {
                    scale = mDrawableWidth / actualDrawableWidth
                } else {
                    scale = mDrawableHeight / actualDrawableHeight
                }
            } else if (mDrawableHeight > 0) { // only height defined
                scale = mDrawableHeight / actualDrawableHeight
            } else { // only width defined
                scale = mDrawableWidth / actualDrawableWidth
            }

            actualDrawableWidth *= scale
            actualDrawableHeight *= scale

            realBounds.right = realBounds.left + Math.round(actualDrawableWidth)
            realBounds.bottom = realBounds.top + Math.round(actualDrawableHeight)

            mPrefixDrawable?.bounds = realBounds
            //}
        } else {
            //for (drawable in drawables) {

            mPrefixDrawable?.bounds =
                Rect(0, 0, mPrefixDrawable?.intrinsicWidth!!, mPrefixDrawable?.intrinsicHeight!!)
            //}
        }
        return mPrefixDrawable
    }

    companion object {

        private const val TYPE_TEXT_VARIATION_PASSWORD = 129
        private const val TYPE_NUMBER_VARIATION_PASSWORD = 18
        private const val DEFAULT_PADDING = 15
    }

    var maskTextWatcher: MaskTextWatcher? = null
    var mask: String? = null
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                removeTextChangedListener(maskTextWatcher)
            } else {
                maskTextWatcher = MaskTextWatcher(this, mask!!)
                addTextChangedListener(maskTextWatcher)
            }
        }

    val rawText: String?
        get() {
            val formatted = text
            return maskTextWatcher?.unformat(formatted) ?: formatted.toString()
        }
    var phoneUtil: PhoneNumberUtil? = PhoneNumberUtil.getInstance()
    private fun mask(countryCode: String?) {

        val countryCode =
            countryCode?.replace("+", "")//getCountryCodeFormString(countryCode?.toUpperCase()!!)

        var formattedNumber = ""
        val exampleNumber =
            phoneUtil?.getExampleNumberForType(
                getCountryCodeForRegion(countryCode?.toInt()!!),
                PhoneNumberUtil.PhoneNumberType.MOBILE
            )
        exampleNumber?.let {
            formattedNumber = exampleNumber.nationalNumber.toString()
            formattedNumber = PhoneNumberUtils.formatNumber(
                countryCode + formattedNumber,
                getDefaultCountryCode(context)

            )
            if (formattedNumber != null) {
                formattedNumber = formattedNumber.substring(countryCode?.length!!).trim()
            }

            formattedNumber = formattedNumber.replace("[0-9]".toRegex(), "#")

        }
        mask = formattedNumber

        text = text
    }
}