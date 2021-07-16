package co.yap.widgets.spinneradapter.searchable

import android.view.View


interface OnItemSelectedListener {
    fun onItemSelected(view: View?, position: Int, id: Long)
    fun onNothingSelected()
}