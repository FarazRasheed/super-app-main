package co.yap.modules.dashboard.home.helpers

import androidx.recyclerview.widget.DiffUtil
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import org.jetbrains.annotations.Nullable

class TransactionDiffCallback(
    private var newTransactions: List<HomeTransactionListData>,
    private var oldTransactions: List<HomeTransactionListData>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTransactions.size
    }

    override fun getNewListSize(): Int {
        return newTransactions.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTransactions[oldItemPosition].date === newTransactions[newItemPosition].date
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTransactions[oldItemPosition].transaction.size == newTransactions[newItemPosition].transaction.size
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return newTransactions[newItemPosition]
    }
}