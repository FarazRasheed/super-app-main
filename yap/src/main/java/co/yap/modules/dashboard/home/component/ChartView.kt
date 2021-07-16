package co.yap.modules.dashboard.home.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import co.yap.R
import co.yap.yapcore.helpers.ThemeColorUtils

@Deprecated("Use ChartViewV2.kt  ")
class ChartView(context: Context, private var barHeight: Int) : View(context),
    View.OnTouchListener, View.OnFocusChangeListener {


    private var barWeight: Int = 26
    //private var barHeight: Int = 0
    //private var minBarHeight: Int = 0
    //private var maxBarHeight: Int = 100
    private var roundRadius: Int = 7
    private var barRadius: Int = 0
    private var seletedColor: Int = 0
    private var isBarHighLighted: Boolean = false
    private var paintShader: Shader? = null
    private var paint: Paint = Paint()
    var rectF: RectF = RectF()


    init {
        paintShader = LinearGradient(
            0f,
            100f,
            width.toFloat(),
            0f,
            context.resources.getColor(R.color.colorDarkGreyGradient),
            context.resources.getColor(R.color.colorLightGreyGradient),
            Shader.TileMode.CLAMP
        )

        seletedColor = R.color.transparent
        customizePaint(context)
        this.performClick()
        setOnTouchListener(this)
    }

    private fun customizeAnimation(context: Context) {

        val fadeInBarAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeInBarAnimation.duration = 300

        this.startAnimation(fadeInBarAnimation)

        fadeInBarAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                paint.shader = null
                paintShader = null
                val pupleSelectedColor = ThemeColorUtils.colorPrimaryAttribute(context)
                paint.color = pupleSelectedColor
                seletedColor = pupleSelectedColor
                invalidate()
                customizePaint(context)
            }

            override fun onAnimationEnd(animation: Animation) {
//                fadeOutBarAnimation()

            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun fadeInAnim(fadeIn: Animation) {
        fadeIn.duration = 300
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

                paintShader = LinearGradient(
                    0f,
                    100f,
                    width.toFloat(),
                    0f,
                    context.resources.getColor(R.color.colorDarkGreyGradient),
                    context.resources.getColor(R.color.colorLightGreyGradient),
                    Shader.TileMode.CLAMP
                )

                val pupleSelectedColor = context.resources.getColor(R.color.greySoft)
                seletedColor = pupleSelectedColor
                invalidate()
                customizePaint(context)
                isBarHighLighted = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

    }

    private fun customizePaint(context: Context) {
        paint.color = seletedColor
        paint.shader = paintShader
        paint.setStyle(Paint.Style.FILL)
        paint.setStrokeWidth(10F)

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        fadeOutBarAnimation()
    }

    @SuppressLint("ResourceAsColor")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                OnBarItemTouchEvent()
            }
        }

        return true
    }

    fun OnBarItemTouchEvent() {
        if (isBarHighLighted) {
            fadeOutBarAnimation()
        }
        customizeAnimation(context)
    }

    private fun fadeOutBarAnimation() {
        val fadeOutBarAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        startAnimation(fadeOutBarAnimation)
        isBarHighLighted = false
        fadeInAnim(fadeOutBarAnimation)
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

//    fun setBarHeight(height: Double) {
//        // layoutParams = LinearLayout.LayoutParams(width, 0, height.toFloat())
//
//        barHeight = (height.toInt())
//        Log.i("amountPercentage", "barHeight is" + barHeight.toString())
//
//        // invalidate()
//    }

    @SuppressLint("ResourceType")
    protected override fun onDraw(canvas: Canvas) {

        rectF.set(0f, 0f, barWeight.toFloat(), barHeight.toFloat())
        canvas.drawRoundRect(rectF, roundRadius.toFloat(), roundRadius.toFloat(), paint)
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(barWeight, barHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(w, h, oldWidth, oldHeight)
        barWeight = w
        barHeight = h
        barRadius = barWeight / 2
    }
}