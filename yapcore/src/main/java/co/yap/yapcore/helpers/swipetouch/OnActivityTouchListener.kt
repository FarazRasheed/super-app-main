package co.yap.yapcore.helpers.swipetouch

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent)
}