package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemContactsBinding
import co.yap.networking.customers.requestdtos.Contact
import co.yap.yapcore.BasePagingBindingRecyclerAdapter
import co.yap.yapcore.databinding.ItemListFooterBinding


class PhoneContactsAdaptor(private val colors: IntArray, retry: () -> Unit) :
    BasePagingBindingRecyclerAdapter<Contact>(retry, diffCallback) {


    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_contacts

    override fun getLayoutIdForFooterType(viewType: Int): Int = R.layout.item_list_footer

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == contentView)
            (holder as YapContactItemViewHolder).onBind(
                getItem(position),
                position,
                onItemClickListener
            )
        else (holder as ListFooterViewHolder).onBind(getState())
    }

    override fun onCreateContentViewHolder(binding: ViewDataBinding): YapContactItemViewHolder {
        return YapContactItemViewHolder(binding as ItemContactsBinding)
    }

    override fun onCreateFooterViewHolder(binding: ViewDataBinding): ListFooterViewHolder {
        return ListFooterViewHolder(binding as ItemListFooterBinding)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem.mobileNo == newItem.mobileNo

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                oldItem == newItem
        }
    }

}
