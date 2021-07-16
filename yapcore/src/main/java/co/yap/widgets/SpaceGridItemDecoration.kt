package co.yap.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by irfan arshad on 6/28/2016.
 */
class SpaceGridItemDecoration(private val space: Int, private val column: Int, private val includeEdge: Boolean) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % this.column // item column

        if (includeEdge) {
            outRect.left =
                this.space - column * this.space / this.column // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * this.space / this.column // (column + 1) * ((1f / spanCount) * spacing)

            if (position < this.column) { // top edge
                outRect.top = this.space
            }
            outRect.bottom = this.space // item bottom
        } else {
            outRect.left = column * this.space / this.column // column * ((1f / spanCount) * spacing)
            outRect.right =
                this.space - (column + 1) * this.space / this.column // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= this.space) {
                outRect.top = this.space // item top
            }
        }
    }
}
