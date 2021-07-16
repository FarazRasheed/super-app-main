package co.yap.sendmoney.addbeneficiary.adaptor

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.sendmoney.databinding.ItemRakBankBinding
import co.yap.networking.customers.responsedtos.sendmoney.RAKBank.Bank
import co.yap.sendmoney.R
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.interfaces.OnItemClickListener


class RAKBankAdaptor(private val list: MutableList<Bank>) :
    BaseBindingRecyclerAdapter<Bank, RAKBankItemViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_rak_bank

    override fun onCreateViewHolder(binding: ViewDataBinding): RAKBankItemViewHolder {
        return RAKBankItemViewHolder(binding as ItemRakBankBinding)
    }

    override fun onBindViewHolder(holder: RAKBankItemViewHolder, position: Int) {
        holder.onBind(list[position], position, onItemClickListener)
    }
}


class RAKBankItemViewHolder(private val itemRakBankBinding: ItemRakBankBinding) :
    RecyclerView.ViewHolder(itemRakBankBinding.root) {

    fun onBind(bank: Bank, position: Int, onItemClickListener: OnItemClickListener?) {
        itemRakBankBinding.viewModel = RAKBankItemViewModel(bank, position, onItemClickListener)
        itemRakBankBinding.executePendingBindings()
    }
}

class RAKBankItemViewModel(
    val bank: Bank?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    fun onViewClicked(view: View) {
        bank?.let {
            onItemClickListener?.onItemClick(view, it, position)
        }
    }

}