package co.yap.modules.dashboard.cards.home.adaptor

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemYapCardBinding
import co.yap.databinding.ItemYapCardEmptyBinding
import co.yap.modules.dashboard.cards.home.viewholder.YapCardEmptyItemViewHolder
import co.yap.modules.dashboard.cards.home.viewholder.YapCardItemViewHolder
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.helpers.Utils

class YapCardsAdaptor(context: Context, private val list: MutableList<Card>) :
    BaseBindingRecyclerAdapter<Card, RecyclerView.ViewHolder>(list) {

    private val empty = 1
    private val actual = 2
    private var dimensions: IntArray = Utils.getCardDimensions(context, 50, 45)

    override fun getLayoutIdForViewType(viewType: Int): Int =
        if (viewType == actual) R.layout.item_yap_card else R.layout.item_yap_card_empty


    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return if (binding is ItemYapCardBinding) YapCardItemViewHolder(binding) else YapCardEmptyItemViewHolder(
            binding as ItemYapCardEmptyBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is YapCardItemViewHolder) {
            holder.onBind(position, list[position], dimensions, onItemClickListener)
        } else {
            if (holder is YapCardEmptyItemViewHolder)
                holder.onBind(position, list[position], dimensions, onItemClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].cardName == "addCard") empty else actual
    }
}
