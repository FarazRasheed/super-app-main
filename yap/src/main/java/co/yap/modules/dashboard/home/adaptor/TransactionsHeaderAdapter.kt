package co.yap.modules.dashboard.home.adaptor

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemEmptyBinding
import co.yap.databinding.ItemTransactionListHeaderBinding
import co.yap.modules.dashboard.home.helpers.transaction.ItemHeaderTransactionsViewModel
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.enums.TxnType
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager

class TransactionsHeaderAdapter(
    private val list: MutableList<HomeTransactionListData>,
    private val adaptorClick: OnItemClickListener
) :
    BaseBindingRecyclerAdapter<HomeTransactionListData, RecyclerView.ViewHolder>(list) {

    private val empty = 1
    private val actual = 2

    override fun getLayoutIdForViewType(viewType: Int): Int =
        if (viewType == actual) R.layout.item_transaction_list_header else R.layout.item_empty

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return if (binding is ItemTransactionListHeaderBinding) HeaderViewHolder(binding) else EmptyItemViewHolder(
            binding as ItemEmptyBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is HeaderViewHolder) {
            holder.onBind(list[position], adaptorClick, position)
        } else {
            if (holder is EmptyItemViewHolder)
                holder.onBind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].totalAmount == "loader") empty else actual
    }

    class EmptyItemViewHolder(private val itemEmptyBinding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(itemEmptyBinding.root) {

        fun onBind(position: Int) {
            itemEmptyBinding.executePendingBindings()
        }
    }

    class HeaderViewHolder(private val itemTransactionListHeaderBinding: ItemTransactionListHeaderBinding) :
        RecyclerView.ViewHolder(itemTransactionListHeaderBinding.root) {

        fun onBind(
            homeTransaction: HomeTransactionListData,
            adaptorClick: OnItemClickListener,
            groupPosition: Int
        ) {

            //itemTransactionListHeaderBinding.tvTransactionDate.text = homeTransaction.date
            //itemTransactionListHeaderBinding.tvTotalAmount.text = homeTransaction.totalAmount

            itemTransactionListHeaderBinding.rvExpandedTransactionsListing.layoutManager =
                LinearLayoutManager(
                    itemTransactionListHeaderBinding.rvExpandedTransactionsListing.context,
                    LinearLayoutManager.VERTICAL, false
                )

            val snapHelper = PagerSnapHelper()
            itemTransactionListHeaderBinding.rvExpandedTransactionsListing.onFlingListener = null
            snapHelper.attachToRecyclerView(itemTransactionListHeaderBinding.rvExpandedTransactionsListing)

            val mutableList = mutableListOf<Transaction>()
            mutableList.addAll(homeTransaction.transaction)

            val adaptor =
                TransactionsListingAdapter(mutableList)
            itemTransactionListHeaderBinding.rvExpandedTransactionsListing.adapter = adaptor
            adaptor.allowFullItemClickListener = true
            adaptor.setItemListener(object : OnItemClickListener {
                override fun onItemClick(view: View, data: Any, pos: Int) {
                    adaptorClick.onItemClick(view, groupPosition, pos)
                }
            })

            var total = 0.0
            homeTransaction.transaction.map {
                when (it.productCode) {
                    TransactionProductCode.RMT.pCode, TransactionProductCode.SWIFT.pCode -> {
                        if (it.txnType == TxnType.DEBIT.type) {
                            val totalFee = (it.postedFees ?: 0.00).plus(it.vatAmount ?: 0.0)
                            total -= ((it.settlementAmount ?: 0.00).plus(totalFee))
                        } else total += (it.settlementAmount ?: 0.0)
                    }
                    else -> {
                        if (it.txnType == TxnType.DEBIT.type) total -= (it.totalAmount
                            ?: 0.0) else total += (it.amount ?: 0.0)
                    }
                }
            }

            var value: String
            when {
                total.toString().startsWith("-") -> {
                    value =
                        ((total * -1).toString().toFormattedCurrency(showCurrency = false,currency = SessionManager.getDefaultCurrency()))
                    value = "- ${SessionManager.getDefaultCurrency()} $value"
                }
                else -> {
                    value = (total.toString().toFormattedCurrency(false,currency = SessionManager.getDefaultCurrency()))
                    value = "+ ${SessionManager.getDefaultCurrency()} $value"
                }
            }

            homeTransaction.totalAmount = value
            itemTransactionListHeaderBinding.viewModel =
                ItemHeaderTransactionsViewModel(homeTransaction)
            itemTransactionListHeaderBinding.executePendingBindings()
        }
    }
}