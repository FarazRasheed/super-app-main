package co.yap.modules.dashboard.cards.analytics.viewmodels

import android.view.View
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.yapcore.interfaces.OnItemClickListener

class AnalyticsItemViewModel(
    val analyticsItem: TxnAnalytic?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    fun onViewClicked(view: View) {
        onItemClickListener?.onItemClick(view, analyticsItem!!, position)
    }

}
