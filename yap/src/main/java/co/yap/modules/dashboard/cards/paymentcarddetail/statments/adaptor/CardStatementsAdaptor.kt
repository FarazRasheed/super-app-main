package co.yap.modules.dashboard.cards.paymentcarddetail.statments.adaptor

import androidx.databinding.ViewDataBinding
import co.yap.R
import co.yap.databinding.ItemCardStatementsBinding
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.viewholder.CardStatementsViewHolder
import co.yap.networking.transactions.responsedtos.CardStatement
import co.yap.yapcore.BaseBindingRecyclerAdapter

class CardStatementsAdaptor(private val list: MutableList<CardStatement>) :
    BaseBindingRecyclerAdapter<CardStatement, CardStatementsViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_card_statements

    override fun onCreateViewHolder(binding: ViewDataBinding): CardStatementsViewHolder {
        return CardStatementsViewHolder(binding as ItemCardStatementsBinding)
    }

    override fun onBindViewHolder(holder: CardStatementsViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position])
    }

}