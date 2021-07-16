package co.yap.widgets.guidedtour.shape

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import co.yap.yapcore.R


class CircleOverlayView : LinearLayout {
    private var bitmap: Bitmap? = null
    var centerX: Float = 10f
    var centerY: Float = 10f
    var canvas: Canvas? = null
    var updateCircle: Boolean = false
    var radius = resources.getDimensionPixelSize(R.dimen._50sdp).toFloat()

    constructor(context: Context?) : super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {

    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        this.canvas = canvas
        if (bitmap == null) {
            createWindowFrame()
        }
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
    }

    protected fun createWindowFrame() {
        bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val osCanvas = Canvas(bitmap!!)
        val outerRectangle =
            RectF(0f, 0f, width.toFloat(), height.toFloat())
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(R.color.colorCoachMarkOverlay)
        osCanvas.drawRect(outerRectangle, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        osCanvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(R.color.colorCoachMarkOverlay)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        val radius = resources.getDimensionPixelSize(R.dimen._50sdp).toFloat()

        canvas?.drawCircle(centerX, centerY, radius, paint)
    }

    override fun invalidate() {

        if (updateCircle) {
            super.invalidate()
            createWindowFrame()

        }
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        bitmap = null
    }
}