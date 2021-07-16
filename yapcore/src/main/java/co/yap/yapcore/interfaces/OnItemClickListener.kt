package co.yap.yapcore.interfaces

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, data: Any, pos: Int)
}