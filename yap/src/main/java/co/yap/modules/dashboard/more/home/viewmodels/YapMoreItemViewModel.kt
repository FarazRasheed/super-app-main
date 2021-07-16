package co.yap.modules.dashboard.more.home.viewmodels

import android.view.View
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.yapcore.interfaces.OnItemClickListener


class YapMoreItemViewModel(
    val moreOption: MoreOption,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, moreOption, position)
    }
}