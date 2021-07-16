package co.yap.sendmoney.home.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.R
import co.yap.sendmoney.databinding.LayoutItemBeneficiaryBinding
import co.yap.yapcore.BaseBindingSearchRecylerAdapter
import co.yap.yapcore.interfaces.OnItemClickListener

class AllBeneficiariesAdapter(
    private val list: MutableList<Beneficiary>
) : BaseBindingSearchRecylerAdapter<Beneficiary, AllBeneficiariesAdapter.AllBeneficiariesItemViewHolder>(
    list
) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.layout_item_beneficiary

    override fun onCreateViewHolder(binding: ViewDataBinding): AllBeneficiariesItemViewHolder {
        return AllBeneficiariesItemViewHolder(binding as LayoutItemBeneficiaryBinding)
    }

    override fun onBindViewHolder(holder: AllBeneficiariesItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
            holder.onBind(list[position], position, onItemClickListener)
    }

    class AllBeneficiariesItemViewHolder(
        private val itemContactsBinding: LayoutItemBeneficiaryBinding
    ) :
        RecyclerView.ViewHolder(itemContactsBinding.root) {

        fun onBind(
            beneficiary: Beneficiary?,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {

            itemContactsBinding.viewModel = BeneficiaryItemViewModel(beneficiary, position, onItemClickListener)
            itemContactsBinding.executePendingBindings()

        }
    }

    override fun filterItem(constraint: CharSequence?, item: Beneficiary): Boolean {
        val filterString = constraint.toString().toLowerCase()
        val nickname = item.title?.toLowerCase() ?: item.fullName()
        val fullName = item.fullName().toLowerCase()

        return nickname.contains(filterString) || fullName.contains(filterString)
    }

}
