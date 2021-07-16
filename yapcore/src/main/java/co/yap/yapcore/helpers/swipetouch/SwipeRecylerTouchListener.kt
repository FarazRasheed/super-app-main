package co.yap.yapcore.helpers.swipetouch

import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SwipeRecylerTouchListener : RecyclerView.OnItemTouchListener, OnActivityTouchListener {
    private val TAG = "RecyclerTouchListener"
    val handler = Handler()
    var act: Activity? = null
    var unSwipeableRows: List<Int>? = null
    var independentViews: List<Int>? = null
    var unClickableRows: List<Int>? = null
    var optionViews: List<Int>? = null
    private val touchSlop = 0
    private val minFlingVel = 0
    private val maxFlingVel = 0
    private val ANIMATION_STANDARD = 300L
    private val ANIMATION_CLOSE = 150L
    private val rView: RecyclerView? = null
    private val bgWidth = 1
    private val bgWidthLeft = 1
    private val mDismissAnimationRefCount = 0
    private val touchedX = 0f
    private val touchedY = 0f
    private val isFgSwiping = false
    private val mSwipingSlop = 0
    private val mVelocityTracker: VelocityTracker? = null
    private val touchedPosition = 0
    private val touchedView: View? = null
    private val mPaused = false
    private val bgVisible = false
    private val fgPartialViewClicked = false
    private val bgVisiblePosition = 0
    private val bgVisibleView: View? = null
    private val isRViewScrolling = false
    private val heightOutsideRView = 0
    private val screenHeight = 0
    private val mLongClickPerformed = false
    private val fgView: View? = null
    private val bgView: View? = null
    private val fgViewID = 0
    private val bgViewID = 0
    private val bgViewIDLeft = 0
    private val fadeViews: ArrayList<Int>? = null
//    private val mRowClickListener: RecyclerView .OnRowClickListener? = null
//    private val mRowLongClickListener: SwipeRecylerTouchListener.OnRowLongClickListener? =
//        null
//    private val mBgClickListener: SwipeRecylerTouchListener.OnSwipeOptionsClickListener? =
//        null
//    private val mBgClickListenerLeft: SwipeRecylerTouchListener.OnSwipeOptionsClickListener? =
//        null
    private val clickable = false
    private val longClickable = false
    private val swipeable = false
    private val swipeableLeftOptions = false
    private val LONG_CLICK_DELAY = 800
    private val longClickVibrate = false
    var mLongPressed: Runnable? = null
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun getTouchCoordinates(ev: MotionEvent) {
    }

}