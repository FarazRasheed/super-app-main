package co.yap.sendmoney.currencyPicker.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.sendmoney.R
import co.yap.sendmoney.currencyPicker.viewmodel.CurrencyItemViewModel
import co.yap.sendmoney.currencyPicker.model.MultiCurrencyWallet
import co.yap.sendmoney.databinding.ItemCurrencyCountryBinding
import co.yap.yapcore.BaseBindingSearchRecylerAdapter
import co.yap.yapcore.interfaces.OnItemClickListener

class CurrencyAdapter(
    private val list: MutableList<MultiCurrencyWallet>
) : BaseBindingSearchRecylerAdapter<MultiCurrencyWallet, CurrencyAdapter.CurrencyItemViewHolder>(
    list
) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_currency_country

    override fun onCreateViewHolder(binding: ViewDataBinding): CurrencyItemViewHolder {
        return CurrencyItemViewHolder(
            binding as ItemCurrencyCountryBinding
        )
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }

    class CurrencyItemViewHolder(
        private val itemCurrencyCountryBinding: ItemCurrencyCountryBinding
    ) :
        RecyclerView.ViewHolder(itemCurrencyCountryBinding.root) {

        fun onBind(
            multiCurrencyWallet: MultiCurrencyWallet?,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {

            itemCurrencyCountryBinding.viewModel =
                CurrencyItemViewModel(
                    multiCurrencyWallet,
                    position,
                    onItemClickListener
                )
            itemCurrencyCountryBinding.executePendingBindings()

        }
    }

    override fun filterItem(constraint: CharSequence?, item: MultiCurrencyWallet): Boolean {
        val filterString = constraint.toString().toLowerCase()
        val currencyName = item.getCurrencySymbolWithName()?.toLowerCase()
        return currencyName.contains(filterString)
    }

}
