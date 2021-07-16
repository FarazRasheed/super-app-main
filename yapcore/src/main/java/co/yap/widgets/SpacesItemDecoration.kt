package co.yap.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration : RecyclerView.ItemDecoration {
    private val spaceLeft: Int
    private val spaceRight: Int
    private val spaceTop: Int
    private val spaceBottom: Int
    private var spaceFirstItem = true

    constructor(space: Int) {
        this.spaceLeft = space
        this.spaceBottom = space
        this.spaceRight = space
        this.spaceTop = space
    }

    constructor(
        spaceLeft: Int,
        spaceRight: Int,
        spaceTop: Int,
        spaceBottom: Int,
        spaceFirstItem: Boolean = true
    ) {
        this.spaceLeft = spaceLeft
        this.spaceBottom = spaceBottom
        this.spaceRight = spaceRight
        this.spaceTop = spaceTop
        this.spaceFirstItem = spaceFirstItem
    }

    constructor(space: Int, spaceFirstItem: Boolean) {
        this.spaceLeft = space
        this.spaceBottom = space
        this.spaceRight = space
        this.spaceTop = space
        this.spaceFirstItem = spaceFirstItem
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = spaceLeft
        outRect.right = spaceRight
        outRect.bottom = spaceBottom
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0 && spaceFirstItem) {
            outRect.top = spaceTop
        } else {
            outRect.top = 0
        }
    }
}