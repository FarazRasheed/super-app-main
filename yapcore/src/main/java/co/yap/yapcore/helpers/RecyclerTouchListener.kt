package co.yap.yapcore.helpers

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerTouchListener(
    context: Context, val checkTouch: Boolean,
    var recyclerView: RecyclerView,
    private val clickListener: ClickListener
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector
    private var mLastMotionX: Int = 0
    private var mLastMotionY: Int = 0
    private var isMoving = false
    private val SWIPE_MIN_DISTANCE = 1

    init {
        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    if (e == null) return false
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        clickListener.onClick(child, recyclerView.getChildAdapterPosition(child))
                    }
                    return true
                }
            })

        recyclerView.addOnItemTouchListener(this)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionX = e.x.toInt()
                mLastMotionY = e.y.toInt()
                isMoving = false
            }
            MotionEvent.ACTION_MOVE -> {
                val x = e.x.toInt()
                val y = e.y.toInt()
                val deltaX = mLastMotionX - x
                RecyclerTouchListener.deltaX = deltaX

                if (Math.abs(deltaX) > SWIPE_MIN_DISTANCE) {
                    isMoving = true
                    if (x > mLastMotionX) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null) {
                            clickListener.onRightSwipe(
                                child,
                                recyclerView.layoutManager?.getPosition(child)!!
                            )
                        }
                    } else {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null) {
                            clickListener.onLeftSwipe(
                                child,
                                recyclerView.layoutManager?.getPosition(child)!!
                            )
                        }
                    }


                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isMoving) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        clickListener.onClick(child, recyclerView.getChildAdapterPosition(child))
                    }
                }


            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLeftSwipe(view: View, position: Int)
        fun onRightSwipe(view: View, position: Int)
    }

    companion object {
        var deltaX: Int = 300
    }
}