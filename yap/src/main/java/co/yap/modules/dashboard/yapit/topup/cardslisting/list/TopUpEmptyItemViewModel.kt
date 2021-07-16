package co.yap.modules.dashboard.yapit.topup.cardslisting.list

import android.view.View
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.interfaces.OnItemClickListener

class TopUpEmptyItemViewModel(
    val topUpCard: TopUpCard?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    // Custom logic if there any and add only observable or mutable data if your really need it.
    // You can also add methods for callbacks from xml
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, topUpCard!!, position)
    }
}