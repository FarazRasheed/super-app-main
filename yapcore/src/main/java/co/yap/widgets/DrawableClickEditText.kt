package co.yap.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatEditText
import co.yap.yapcore.R
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.getScreenWidth
import kotlin.math.abs


class DrawableClickEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {
    private var mPopupWindow: PopupWindow? = null
    private var popupView: View? = null

    private var drawableRight: Drawable? = null
    private var drawableLeft: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null
    private var tvPopupContent: TextView? = null
    private var positionX: Int = 0
    private var positionY: Int = 0
    private var isDrawableShownWhenTextIsEmpty = true
    private var popupTextValue = ""
    private var onDrawableClickListener: OnDrawableClickListener? = null
    private var defaultClickListener: OnDrawableClickListener? = null

    private val defaultClickListenerAdapter: OnDrawableClickListener =
        object : OnDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                when (target) {
                    DrawablePosition.BOTTOM -> {

                    }
                    DrawablePosition.LEFT -> {

                    }
                    DrawablePosition.TOP -> {

                    }
                    DrawablePosition.RIGHT -> {
                        val xoff = Utils.getDimensionInPercent(context,true,6)
                        showAsPopUp(this@DrawableClickEditText, xoff, 0)
                    }
                }
            }
        }


    init {
        parseAttributes(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.DrawableClickEditText
            )
        )

    }

    private fun parseAttributes(obtainStyledAttributes: TypedArray) {
        isDrawableShownWhenTextIsEmpty = obtainStyledAttributes.getBoolean(
            R.styleable.DrawableClickEditText_isDrawableShownWhenTextIsEmpty,
            isDrawableShownWhenTextIsEmpty
        )
        popupTextValue = resources.getText(
            obtainStyledAttributes
                .getResourceId(
                    R.styleable.DrawableClickEditText_popupContentValue,
                    R.string.empty_string
                )
        ).toString()
        obtainStyledAttributes.recycle()
        hasDrawable(isDrawableShownWhenTextIsEmpty)
        defaultClickListener = defaultClickListenerAdapter
    }

    fun hasDrawable(value: Boolean) {
        isDrawableShownWhenTextIsEmpty = value
        if (!isDrawableShownWhenTextIsEmpty) {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        invalidate()
    }

    private fun initPopupWindow() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(R.layout.pop_up_view, null)
        tvPopupContent = popupView?.findViewById(R.id.tvContent)
        setPopupContent()
        val popUpWindowWidth = getScreenWidth() - Utils.getDimensionInPercent(context, true, 11)
        mPopupWindow =
            PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        mPopupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
    }

    private fun setPopupContent() {
        tvPopupContent?.text = popupTextValue
    }

    private fun showAsPopUp(anchor: View) {
        showAsPopUp(anchor, 0, 0)
    }

    private fun showAsPopUp(anchor: View, xoff: Int, yoff: Int) {
        initPopupWindow()
        mPopupWindow?.animationStyle = R.style.AnimationUpPopup
        anchor.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val height = anchor.measuredHeight
        val ivHeight = drawableRight?.intrinsicHeight ?: 0
        val location = IntArray(2)
        anchor.getLocationInWindow(location)
        mPopupWindow?.showAtLocation(
            anchor,
            Gravity.TOP,
            location[0] - xoff,
            location[1] - height - ivHeight*2
        )
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     * @param isCancelable
     */
    private fun setCancelable(isCancelable: Boolean) {
        if (isCancelable) {
            mPopupWindow?.isOutsideTouchable = true
            mPopupWindow?.isFocusable = true
        } else {
            mPopupWindow?.isOutsideTouchable = false
            mPopupWindow?.isFocusable = false
        }
    }

    override fun setCompoundDrawables(
        leftDrawable: Drawable?,
        topDrawable: Drawable?,
        rightDrawable: Drawable?,
        bottomDrawable: Drawable?
    ) {
        if (leftDrawable != null) drawableLeft = leftDrawable
        if (rightDrawable != null) drawableRight = rightDrawable
        if (topDrawable != null) drawableTop = topDrawable
        if (bottomDrawable != null) drawableBottom = bottomDrawable
        super.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect?
        val editText = this
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (char.isEmpty()) {
                    if (!isDrawableShownWhenTextIsEmpty) editText.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        0,
                        0
                    )
                } else editText.setCompoundDrawables(
                    drawableLeft,
                    drawableTop,
                    drawableRight,
                    drawableBottom
                )
            }


        })
        if (event.action == MotionEvent.ACTION_DOWN) {
            positionX = event.x.toInt()
            positionY = event.y.toInt()

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                bounds = drawableLeft?.bounds
                setupDrawableLeftClick(bounds, event)
            }

            if (drawableRight != null) {
                bounds = drawableRight?.bounds
                setupDrawableRightClick(bounds, event)
            }

            if (drawableTop != null) {
                bounds = drawableTop?.bounds
                setupDrawableTopClick(bounds, event)
            }

            if (drawableBottom != null) {
                bounds = drawableBottom?.bounds
                setupDrawableBottomClick(bounds, event)
            }


        }
        return super.onTouchEvent(event)
    }

    private fun setupDrawableBottomClick(bounds: Rect?, event: MotionEvent) {
        val extraClickingArea = 13
        if (abs((width - paddingLeft - paddingRight) / 2 + paddingLeft - positionX) <= bounds!!.width() / 2 + extraClickingArea) {
            onDrawableClickListener?.onClick(DrawablePosition.BOTTOM)
            defaultClickListener?.onClick(DrawablePosition.BOTTOM)
            event.action = MotionEvent.ACTION_CANCEL
        }
    }

    private fun setupDrawableTopClick(bounds: Rect?, event: MotionEvent) {
        val extraClickingArea = 13
        if (abs((width - paddingLeft - paddingRight) / 2 + paddingLeft - positionX) <= bounds!!.width() / 2 + extraClickingArea) {
            onDrawableClickListener?.onClick(DrawablePosition.TOP)
            defaultClickListener?.onClick(DrawablePosition.TOP)
            event.action = MotionEvent.ACTION_CANCEL
        }
    }

    private fun setupDrawableLeftClick(bounds: Rect?, event: MotionEvent) {
        var xClickPosition: Int
        var yClickPosition: Int
        /*
         * @return pixels into dp
         */
        val extraClickArea = (13 * resources.displayMetrics.density + 0.5).toInt()

        xClickPosition = positionX
        yClickPosition = positionY

        if (!bounds!!.contains(positionX, positionY)) {
            /** Gives some extra space for tapping.  */
            xClickPosition = positionX - extraClickArea
            yClickPosition = positionY - extraClickArea

            if (xClickPosition <= 0) xClickPosition = positionX
            if (yClickPosition <= 0) yClickPosition = positionY

            /** Creates square from the smallest value  from x or y*/
            if (xClickPosition < yClickPosition) yClickPosition = xClickPosition
        }

        if (bounds.contains(xClickPosition, yClickPosition) && onDrawableClickListener != null) {
            onDrawableClickListener?.onClick(DrawablePosition.LEFT)
            defaultClickListener?.onClick(DrawablePosition.LEFT)
            event.action = MotionEvent.ACTION_CANCEL

        }
    }

    private fun setupDrawableRightClick(bounds: Rect?, event: MotionEvent) {
        var xClickPosition: Int
        var yClickPosition: Int
        val extraClickingArea = 13

        xClickPosition = positionX + extraClickingArea
        yClickPosition = positionY - extraClickingArea

        /**
         * It right drawable -> subtract the value of x from the width of view. so that width - tapped area                     * will result in x co-ordinate in drawable bound.
         */
        xClickPosition = width - xClickPosition
        if (xClickPosition <= 0) xClickPosition += extraClickingArea

        /* If after calculating for extra clickable area is negative.
         * assign the original value so that after subtracting
         * extra clicking area value doesn't go into negative value.
         */

        if (yClickPosition <= 0) yClickPosition = positionY

        /**If drawable bounds contains the x and y points then move ahead. */
        if (bounds!!.contains(
                xClickPosition,
                yClickPosition
            ) && (onDrawableClickListener != null || defaultClickListener != null)
        ) {
            onDrawableClickListener?.onClick(DrawablePosition.RIGHT)
            defaultClickListener?.onClick(DrawablePosition.RIGHT)
            event.action = MotionEvent.ACTION_CANCEL
        }
    }

    fun setDrawableClickListener(OnDrawableClickListener: OnDrawableClickListener) {
        this.onDrawableClickListener = OnDrawableClickListener
    }

    interface OnDrawableClickListener {
        fun onClick(target: DrawablePosition)
    }
    @Keep
    enum class DrawablePosition {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

}