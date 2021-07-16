package co.yap.modules.dashboard.yapit.addmoney.landing

import android.view.View
import co.yap.yapcore.interfaces.OnItemClickListener


class YapItAddMoneyLandingItemVM(
    var addMoneyOptions: AddMoneyLandingOptions,
    var position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, addMoneyOptions, position)
    }
}