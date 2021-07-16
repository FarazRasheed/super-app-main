package co.yap.widgets.spinneradapter.searchable

import android.view.View


interface ISpinnerSelectedView {
    fun getNoSelectionView(): View?
    fun getSelectedView(position: Int): View?
}