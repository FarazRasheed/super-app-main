package co.yap.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.isEmpty
import com.google.android.material.textfield.TextInputLayout
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

@Keep
class CollapsedColoredHintTextInputLayout : TextInputLayout {
    private var collapseHintMethod: Method? = null
    private var hintTextColorNormal = 0
    private var hintTextColorSelected = 0
    private var bounds: Rect? = null
    private var recalculateMethod: Method? = null
    private var collapsingTextHelper: Any? = null
    private var drawablePadding: Int = -1

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        initializeCustomAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        initializeCustomAttrs(context, attrs)
    }

    fun init() {
        initForHint()
        isHintAnimationEnabled = false
        try {

            this.post {
                initializeTextWatcher()
            }
            collapseHintMethod = TextInputLayout::class.java.getDeclaredMethod(
                "collapseHint",
                Boolean::class.javaPrimitiveType
            )
            collapseHintMethod?.isAccessible = true
        } catch (ignored: Exception) {
        }
    }


    private fun initializeCustomAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable
                .CollapsedColoredHintTextInputLayout, 0, 0
        )
        try {
            drawablePadding = typedArray.getDimensionPixelOffset(
                R.styleable.CollapsedColoredHintTextInputLayout_android_drawablePadding, -1
            )
            typedArray.getColor(
                R.styleable.CollapsedColoredHintTextInputLayout_hintTextColorNormal,
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)
                    .also { if (it != 0) hintTextColorNormal = it }
            )
            typedArray.getColor(
                R.styleable.CollapsedColoredHintTextInputLayout_hintTextColorSelected,
                ContextCompat.getColor(getContext(), R.color.greyDark)
                    .also { if (it != 0) hintTextColorSelected = it }
            )

        } finally {
            typedArray.recycle()
        }
    }

    override fun invalidate() {
        super.invalidate()
        try {
            collapseHintMethod?.invoke(this, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializeTextWatcher() {
        editText?.let {
            hintTextColor =
                if (isEmpty(it)) ColorStateList.valueOf(hintTextColorNormal) else ColorStateList.valueOf(
                    hintTextColorSelected
                )
        }
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                hintTextColor =
                    if (isEmpty(s)) ColorStateList.valueOf(hintTextColorNormal) else ColorStateList.valueOf(
                        hintTextColorSelected
                    )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    /**
     * Adjust Bounds of the view for padding
     * and changes for the view
     */
    private fun adjustBounds() {
        if (collapsingTextHelper == null)
            return
        try {
            bounds?.left =
                if (drawablePadding > -1) drawablePadding else (editText?.left
                    ?: 0) + (editText?.paddingLeft ?: 0)
            // setCollapsedBounds?.invoke(collapsingTextHelper , bounds)
            recalculateMethod?.invoke(collapsingTextHelper)
            /// collapseHintMethod?.invoke(this, false)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    /**
     * On layout changes
     * @param changed Boolean
     * @param left Int
     * @param top Int
     * @param right Int
     * @param bottom Int
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        adjustBounds()
    }

    /**
     * Init function call the TextInputLayout class and the secondary internal class CollapsingTextHelper
     * @see TextInputLayout
     */
    private fun initForHint() {
        try {
            //Search internal and private class over object called as variable
            val cthField = TextInputLayout::class.java.getDeclaredField(COLLAPSING_HELPER)
            cthField.isAccessible = true
            collapsingTextHelper = cthField.get(this)

            //Search in private class the other component to create a view
            val boundsField = collapsingTextHelper?.javaClass?.getDeclaredField(COLLAPSED_BOUNDS)
            boundsField?.isAccessible = true
            bounds = boundsField?.get(collapsingTextHelper) as Rect
            // setCollapsedBounds = collapsingTextHelper?.javaClass?.getDeclaredMethod(SETCollapsedBounds , Rect::class.javaPrimitiveType)
            recalculateMethod = collapsingTextHelper?.javaClass?.getDeclaredMethod(RECALCULATE)

        } catch (e: NoSuchFieldException) {
            collapsingTextHelper = null
            bounds = null
            recalculateMethod = null
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            collapsingTextHelper = null
            bounds = null
            recalculateMethod = null
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            collapsingTextHelper = null
            bounds = null
            recalculateMethod = null
            e.printStackTrace()
        }
    }

    /**
     * Companion Object
     */
    companion object {
        const val COLLAPSING_HELPER = "collapsingTextHelper"
        const val COLLAPSED_BOUNDS = "collapsedBounds"
        const val RECALCULATE = "recalculate"
        const val SETCollapsedBounds = "setCollapsedBounds"
    }
}
