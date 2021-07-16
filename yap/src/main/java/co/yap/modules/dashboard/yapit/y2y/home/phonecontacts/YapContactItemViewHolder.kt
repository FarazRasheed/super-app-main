package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemContactsBinding
import co.yap.networking.customers.requestdtos.Contact
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener


class YapContactItemViewHolder(private val itemContactsBinding: ItemContactsBinding) :
    RecyclerView.ViewHolder(itemContactsBinding.root) {

    fun onBind(
        contact: Contact?,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        itemContactsBinding.viewModel =
            YapContactItemViewModel(contact, position, onItemClickListener)
        itemContactsBinding.executePendingBindings()
    }
}