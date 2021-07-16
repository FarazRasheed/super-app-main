package co.yap.widgets.guidedtour

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import co.yap.widgets.CoreButton
import co.yap.widgets.guidedtour.description.*
import co.yap.widgets.guidedtour.models.GuidedTourViewDetail
import co.yap.widgets.guidedtour.shape.CircleOverlayView
import co.yap.yapcore.R

class TourSetup(
    private val activity: Activity,
    private val guidedTourViewViewsList: ArrayList<GuidedTourViewDetail>
) : Dialog(activity, android.R.style.Theme_Light) {

    private var currentViewId: Int = 0
    private var descBox: BubbleMessageView.Builder? = null
    private var descBoxView: BubbleMessageView? = null
    private var showCase: BubbleShowCase? = null

    private var layer: CircleOverlayView? = null
    private var rootMain: RelativeLayout? = null
    private var skip: CoreButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialogue_coach_mark_overlay)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        layer = findViewById(R.id.circleView)
        rootMain = findViewById(R.id.rlMain)
        skip = findViewById(R.id.skip)
        skip?.setOnClickListener {
            dismiss()
        }
    }

    fun startTour() {
        show()
        updateCircle()
        addDescBox()
        Handler().postDelayed({
            skip?.visibility = View.VISIBLE
            updateSkipButtonPosition()
//            skip!!.alpha = 0f
//            skip!!.animate().alpha(1f).setDuration(500)
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator?) {
//                        super.onAnimationEnd(animation)
//                        skip?.visibility = View.VISIBLE
//                    }
//                })
        }, 500)
    }

    private fun updateSkipButtonPosition() {
        getCurrentItem()?.let {
            val isOnTop = TourUtils.isViewLocatedAtHalfTopOfTheScreen(context, it.view)
            val sh = TourUtils.getScreenHeight(context).toFloat()
            skip?.y = if (!isOnTop) sh.div(9f) - skip!!.height else sh - sh.div(9f) - skip!!.height
            skip!!.animate().y(skip?.y!!.toFloat()).setDuration(1000).start()
        }
    }

    private fun addDescBox() {
        val animation = TourUtils.getFadeInAnimation(0, 500)
        getCurrentItem()?.let {
            showCase = BubbleShowCase(getDescBox(activity, it))
            descBox = getBubbleMessageViewBuilder(it)
            val showCaseParams = showCase?.addBubbleMessageViewDependingOnTargetView(
                it.view,
                descBox!!
            )
            descBoxView = descBox?.build()
            descBoxView?.y = descBoxView?.y?.minus(it.padding) ?: 0f
            descBoxView?.layoutParams = showCaseParams
            val p = descBoxView?.layoutParams
            rootMain?.addView(
                TourUtils.setAnimationToView(descBoxView!!, animation)
                , 0
            )
            descBoxView?.bringToFront()
        }
    }

    private fun updateCircle() {
        getCurrentItem()?.let {
            layer?.radius = it.circleRadius
            layer?.centerX = it.view.locationOnScreen.x.toFloat() + (it.view.width / 2)
            when {
                TourUtils.isViewLocatedAtBottomOfTheScreen(context, it.view, 200) -> {
                    layer?.centerY = it.view.locationOnScreen.y.toFloat() - 125
                }
                TourUtils.isViewLocatedAtTopOfTheScreen(context, it.view, 200) -> {
                    layer?.centerY = it.view.locationOnScreen.y.toFloat() - 50
                }
                else -> {
                    layer?.centerY = it.view.locationOnScreen.y.toFloat()
                }
            }
            layer?.invalidate()
        }
    }

    private fun updateDescriptionBox() {
        getCurrentItem()?.let {
            val builder = getBubbleMessageViewBuilder(it)
            val arrowPosition = getTooltipPosition(it.view).first()
            val yPos = TourUtils.getAxisYpositionOfViewOnScreen(it.view).toFloat()
            val yPadding =
                if (arrowPosition == BubbleShowCase.ArrowPosition.TOP) yPos + it.padding else yPos - it.padding
            descBoxView?.animate()
                ?.y(yPadding)
                ?.setDuration(500)?.start()
            descBoxView?.setAttributes(builder)
        }
    }

    private val listener = object : OnTourItemClickListener {
        override fun onItemClick(pos: Int) {
            currentViewId += 1
            if (currentViewId < guidedTourViewViewsList.size) {
                layer?.updateCircle = true
                updateDescriptionBox()
                updateCircle()
                updateSkipButtonPosition()
            } else {
                dismiss()
            }
        }
    }

    private fun getBubbleMessageViewBuilder(tourDetail: GuidedTourViewDetail): BubbleMessageView.Builder {
        return BubbleMessageView.Builder(activity)
            .from(activity)
            .arrowPosition(getTooltipPosition(tourDetail.view))
            .backgroundColor(activity.getColor(R.color.white))
            .title(tourDetail.title)
            .subtitle(tourDetail.description)
            .pageNo("${currentViewId + 1}/${guidedTourViewViewsList.size}")
            .targetViewScreenLocation(getTargetViewScreenLocation(tourDetail.view))
            .listener(object : OnBubbleMessageViewListener {
                override fun onBubbleClick() {
                    listener.onItemClick(currentViewId)
                }

                override fun onCloseActionImageClick() {
                    listener.onItemClick(currentViewId)
                }
            })
    }

    private fun getDescBox(
        context: Context,
        guidedTourViewDetail: GuidedTourViewDetail
    ): BubbleShowCaseBuilder {
        return BubbleShowCaseBuilder(activity)
            .title(guidedTourViewDetail.title)
            .description(guidedTourViewDetail.description)
            .backgroundColor(context.getColor(R.color.white)) //Bubble background color
            .targetView(guidedTourViewDetail.view)
    }


    private fun getTooltipPosition(targetView: View): ArrayList<BubbleShowCase.ArrowPosition> {
        val list: ArrayList<BubbleShowCase.ArrowPosition> = arrayListOf()
        if (TourUtils.isViewLocatedAtHalfTopOfTheScreen(
                activity,
                targetView
            )
        ) list.add(BubbleShowCase.ArrowPosition.TOP) else list.add(
            BubbleShowCase.ArrowPosition.BOTTOM
        )
        return list
    }

    private fun getTargetViewScreenLocation(
        targetView: View
    ): RectF {
        return RectF(
            showCase?.getXposition(targetView)?.toFloat()!!,
            showCase?.getYposition(targetView)?.toFloat()!!,
            showCase?.getXposition(targetView)?.toFloat()!! + targetView.width,
            showCase?.getYposition(targetView)?.toFloat()!! + targetView.height
        )
    }

    private fun getCurrentItem(): GuidedTourViewDetail? {
        return if (!guidedTourViewViewsList.isNullOrEmpty() && currentViewId < guidedTourViewViewsList.size) guidedTourViewViewsList[currentViewId] else null
    }
}

interface OnTourItemClickListener {
    fun onItemClick(pos: Int)
}