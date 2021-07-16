package co.yap.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.yapcore.R

class HorizontalNonScrollRecyclerView(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    var canScroll = false
    var scrollEnable = true

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.HorizontalNonScrollRecyclerView)
        canScroll =
            typedArray.getBoolean(R.styleable.HorizontalNonScrollRecyclerView_canScroll, false)
        scrollEnable =
            typedArray.getBoolean(
                R.styleable.HorizontalNonScrollRecyclerView_scroll_enable,
                scrollEnable
            )
        scrollEnable =
            typedArray.getBoolean(
                R.styleable.HorizontalNonScrollRecyclerView_scroll_enable,
                scrollEnable
            )
        typedArray.recycle()

        layoutManager = CustomLinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            true
        )

    }

    inner class CustomLinearLayoutManager(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean
    ) : LinearLayoutManager(context, orientation, reverseLayout) {

        override fun canScrollHorizontally(): Boolean {
            return canScroll
        }

        override fun canScrollVertically(): Boolean {
            return false
        }
    }

    override fun scrollToPosition(position: Int) {
        if (!scrollEnable) {
            return
        }
        super.scrollToPosition(position)
    }

    override fun smoothScrollToPosition(position: Int) {
        if (!scrollEnable) {
            return
        }
        super.smoothScrollToPosition(position)
    }

    override fun smoothScrollBy(dx: Int, dy: Int) {
        if (!scrollEnable) {
            return
        }
        super.smoothScrollBy(dx, dy)
    }

    override fun scrollTo(x: Int, y: Int) {
        if (!scrollEnable) {
            return
        }
        super.scrollTo(x, y)
    }

    override fun scrollBy(x: Int, y: Int) {
        if (!scrollEnable) {
            return
        }
        super.scrollBy(x, y)
    }

}