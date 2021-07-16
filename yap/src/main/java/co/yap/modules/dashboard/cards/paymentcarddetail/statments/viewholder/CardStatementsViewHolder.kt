package co.yap.modules.dashboard.cards.paymentcarddetail.statments.viewholder

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemCardStatementsBinding
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.viewmodels.CardStatementItemViewModel
import co.yap.networking.transactions.responsedtos.CardStatement


class CardStatementsViewHolder(private val itemCardStatementsBinding: ItemCardStatementsBinding) :
    RecyclerView.ViewHolder(itemCardStatementsBinding.root) {

    fun onBind(cardStatement: CardStatement) {
        itemCardStatementsBinding.viewModel = CardStatementItemViewModel(cardStatement)
        itemCardStatementsBinding.executePendingBindings()
    }
}
