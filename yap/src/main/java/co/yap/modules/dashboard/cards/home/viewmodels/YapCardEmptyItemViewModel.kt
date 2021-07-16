package co.yap.modules.dashboard.cards.home.viewmodels

import android.view.View
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.interfaces.OnItemClickListener

class YapCardEmptyItemViewModel(
    val paymentCard: Card?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    // Custom logic if there any and add only observable or mutable dataList if your really need it.
    // You can also add methods for callbacks from xml
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, paymentCard!!, position)
    }
}