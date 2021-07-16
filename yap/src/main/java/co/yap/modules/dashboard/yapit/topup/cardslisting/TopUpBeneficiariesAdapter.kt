package co.yap.modules.dashboard.yapit.topup.cardslisting

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemTopupCardEmptyBinding
import co.yap.databinding.ItemTopupCardsBinding
import co.yap.modules.dashboard.yapit.topup.cardslisting.list.TopUpEmptyItemViewModel
import co.yap.modules.dashboard.yapit.topup.cardslisting.list.TopUpItemViewModel
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener


class TopUpBeneficiariesAdapter(
    context: Context,
    val list: MutableList<TopUpCard>
) :
    BaseBindingRecyclerAdapter<TopUpCard, RecyclerView.ViewHolder>(list) {

    private val empty = 1
    private val actual = 2
    private var dimensions: IntArray = Utils.getCardDimensions(context, 65, 23)

    override fun getLayoutIdForViewType(viewType: Int): Int =
        if (viewType == actual) R.layout.item_topup_cards else R.layout.item_topup_card_empty


    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return if (binding is ItemTopupCardsBinding) TopUpCardViewHolder(binding) else TopUpEmptyItemViewHolder(
            binding as ItemTopupCardEmptyBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is TopUpCardViewHolder) {
            holder.onBind(position, list[position], dimensions, onItemClickListener)
        } else {
            if (holder is TopUpEmptyItemViewHolder)
                holder.onBind(position, list[position], dimensions, onItemClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].alias == "addCard") empty else actual
    }


    inner class TopUpCardViewHolder(val binding: ItemTopupCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun onBind(
            position: Int,
            paymentCard: TopUpCard,
            dimensions: IntArray,
            onItemClickListener: OnItemClickListener?
        ) {
            binding.parent.alpha = 0.3f
            val params = binding.parent.layoutParams as RecyclerView.LayoutParams
            params.width = dimensions[0]
            params.height = dimensions[1]
            binding.parent.layoutParams = params

            binding.viewModel =
                TopUpItemViewModel(paymentCard, position, onItemClickListener)
            binding.executePendingBindings()
        }
    }

    class TopUpEmptyItemViewHolder(val itemTopUpCardEmptyBinding: ItemTopupCardEmptyBinding) :
        RecyclerView.ViewHolder(itemTopUpCardEmptyBinding.root) {

        fun onBind(
            position: Int,
            topUpCard: TopUpCard,
            dimensions: IntArray,
            onItemClickListener: OnItemClickListener?
        ) {

            val params =
                itemTopUpCardEmptyBinding.lycard.layoutParams as RecyclerView.LayoutParams
            params.width = dimensions[0]
            params.height = dimensions[1]
            itemTopUpCardEmptyBinding.lycard.layoutParams = params

            itemTopUpCardEmptyBinding.viewModel =
                TopUpEmptyItemViewModel(topUpCard, position, onItemClickListener)
            itemTopUpCardEmptyBinding.executePendingBindings()
        }
    }
}