package co.yap.modules.dashboard.home.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import co.yap.R

class ChartViewV2(context: Context, attrs: AttributeSet) : View(context, attrs),
    View.OnTouchListener, View.OnFocusChangeListener {
    private var barWeight: Int = 26
    var barHeight: Float = 99f
        set(value) {
            field = ((getParentViewHeight() / 2) * (value)) + 3
            isBarValueSet = true

            layoutParams.height = field.toInt()//context.dip2px(10f)
            invalidate()
        }
    private var roundRadius: Int = 8
    private var barRadius: Int = 0
    private var isBarValueSet: Boolean = false
    var needAnimation = false
    private var paintShader: Shader? = null
    private var paint: Paint = Paint()
    var rectF: RectF = RectF()

    init {
        if (!isInEditMode) {
            paintShader =
                getPaintShader(R.color.colorDarkGreyGradient, R.color.colorLightGreyGradient)
            customizePaint(context)
        } else {
            paintShader =
                getPaintShader(R.color.colorPrimary, R.color.colorPrimary)
            customizePaint(context)
        }
    }

    private fun customizePaint(context: Context) {
        paint.shader = paintShader
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 10F
    }

    @SuppressLint("ResourceType")
    protected override fun onDraw(canvas: Canvas) {
        //height * (1 - (barHeight ?: 0f))
        try {
            rectF.set(0f, 0f, barWeight.toFloat(), getParentViewHeight())
            canvas.drawRoundRect(rectF, roundRadius.toFloat(), roundRadius.toFloat(), paint)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        fadeOutBarAnimation()
    }

    @SuppressLint("ResourceAsColor")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                //OnBarItemTouchEvent()
            }
        }
        return true
    }

    private fun fadeOutBarAnimation() {
        val fadeOutBarAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        fadeOutBarAnimation.duration = if (needAnimation) 300 else 0
        fadeOutBarAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                paintShader =
                    getPaintShader(R.color.colorDarkGreyGradient, R.color.colorLightGreyGradient)
                invalidate()
                customizePaint(context)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        startAnimation(fadeOutBarAnimation)
    }

    private fun customizeAnimation(context: Context) {
        val fadeInBarAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeInBarAnimation.duration = if (needAnimation) 400 else 300
        this.startAnimation(fadeInBarAnimation)
        fadeInBarAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                paintShader = getPaintShader(R.color.colorPrimary, R.color.colorPrimary)
                invalidate()
                customizePaint(context)
            }

            override fun onAnimationEnd(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun unSelectHighlightedBarOnGraphClick(highlighted: Boolean) {
        fadeOutBarAnimation()
    }

    fun unSelectHighlightedBarOnTransactionCellClick(selectBar: Boolean) {
        if (selectBar) {
            fadeOutBarAnimation()
            customizeAnimation(context)
        } else {
            fadeOutBarAnimation()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(w, h, oldWidth, oldHeight)
        barWeight = w
        //barHeight = h.toFloat()
        barRadius = barWeight / 2
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        unSelectHighlightedBarOnTransactionCellClick(selected)
    }

    private fun getParentView() = parent as View
    private fun getParentViewHeight() =
        resources.getDimension(R.dimen._80sdp)// getParentView().height

    private fun getParentViewWidth() = getParentView().width
    private fun getPaintShader(color0: Int, color1: Int): Shader {
//        return LinearGradient(
//            0f,
//            barHeight,
//            width.toFloat(),
//            barHeight,
//            intArrayOf(ContextCompat.getColor(context, color0),
//                ContextCompat.getColor(context, color1)),null,
//            Shader.TileMode.CLAMP
//        )
//        return LinearGradient(
//            0f,width.toFloat(),
//            barHeight,
//            barHeight,
//            ContextCompat.getColor(context, color1),
//            ContextCompat.getColor(context, color0),
//            Shader.TileMode.CLAMP
//        )
        return LinearGradient(
            0f, 0f,
            0f,
            barHeight,
            ContextCompat.getColor(context, color1),
            ContextCompat.getColor(context, color0),
            Shader.TileMode.CLAMP
        )
    }

}