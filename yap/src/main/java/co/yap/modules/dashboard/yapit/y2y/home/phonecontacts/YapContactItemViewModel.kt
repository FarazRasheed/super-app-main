package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import android.view.View
import co.yap.networking.customers.requestdtos.Contact
import co.yap.yapcore.interfaces.OnItemClickListener

class YapContactItemViewModel(
    val contact: Contact?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    fun onViewClicked(view: View) {
        onItemClickListener?.onItemClick(view, contact!!, position)
    }

}