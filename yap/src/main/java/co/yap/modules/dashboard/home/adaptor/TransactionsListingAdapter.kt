package co.yap.modules.dashboard.home.adaptor

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.core.widget.ImageViewCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemTransactionListBinding
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.translation.Translator.getString
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.enums.TransactionStatus
import co.yap.yapcore.enums.TxnType
import co.yap.yapcore.helpers.DateUtils.FORMAT_TIME_12H
import co.yap.yapcore.helpers.ImageBinding
import co.yap.yapcore.helpers.extentions.*

class TransactionsListingAdapter(private val list: MutableList<Transaction>) :
    BaseBindingRecyclerAdapter<Transaction, RecyclerView.ViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_transaction_list

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder as TransactionListingViewHolder).onBind(list[position])
    }

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return TransactionListingViewHolder(binding as ItemTransactionListBinding)
    }

    class TransactionListingViewHolder(private val itemTransactionListBinding: ItemTransactionListBinding) :
        RecyclerView.ViewHolder(itemTransactionListBinding.root) {

        fun onBind(transaction: Transaction) {
            val context: Context = itemTransactionListBinding.tvCurrency.context
            handleProductBaseCases(context, transaction)

            transaction.transactionNote?.let {
                itemTransactionListBinding.tvTransactionNote.text = it
            }

            itemTransactionListBinding.tvTransactionNote.visibility =
                if (transaction.transactionNote.isNullOrEmpty() || transaction.transactionNote.equals(
                        "null"
                    )
                ) View.GONE else View.VISIBLE
            itemTransactionListBinding.tvTransactionStatus.text = transaction.getTransactionStatus()
            itemTransactionListBinding.tvTransactionStatus.visibility =
                if (transaction.getTransactionStatus().isEmpty()) View.GONE else View.VISIBLE
            itemTransactionListBinding.tvCurrency.text = transaction.getCurrency()
            itemTransactionListBinding.ivIncoming.setImageResource(transaction.getTransactionTypeIcon())

            itemTransactionListBinding.ivIncoming.background =
                if (transaction.getTransactionTypeIcon() == co.yap.yapcore.R.drawable.ic_time) context.getDrawable(
                    R.drawable.bg_round_white
                ) else
                    context.getDrawable(android.R.color.transparent)

            itemTransactionListBinding.tvTransactionAmount.text =
                transaction.getFormattedTransactionAmount()
            setContentDataColor(transaction, itemTransactionListBinding)

        }

        private fun handleProductBaseCases(context: Context, transaction: Transaction) {
            val transactionTitle = transaction.getTransactionTitle()
            val txnIconResId = transaction.getTransactionIcon()
            val categoryTitle: String =
                transaction.getTransactionTypeTitle()
            transaction.productCode?.let {
                if (TransactionProductCode.Y2Y_TRANSFER.pCode == it) {
                    setY2YUserImage(transaction, itemTransactionListBinding)
                } else {
                    if (txnIconResId != -1) {
                        itemTransactionListBinding.ivTransaction.setImageResource(txnIconResId)
                        if (transaction.isTransactionCancelled())
                            itemTransactionListBinding.ivTransaction.alpha = 0.5f
                    } else {
                        setInitialsAsTxnImage(transaction, itemTransactionListBinding)
                        if (transaction.isTransactionCancelled())
                            itemTransactionListBinding.ivTransaction.alpha = 0.5f
                    }

                    ImageViewCompat.setImageTintList(
                        itemTransactionListBinding.ivTransaction,
                        ColorStateList.valueOf(context.getColors(R.color.colorPrimary))
                    )
                }
            }

            itemTransactionListBinding.tvTransactionName.text = transactionTitle
            itemTransactionListBinding.tvTransactionTimeAndCategory.text = getString(
                context,
                R.string.screen_fragment_home_transaction_time_category,
                transaction.getFormattedTime(outputFormat = FORMAT_TIME_12H), categoryTitle
            )
        }

        private fun setY2YUserImage(
            transaction: Transaction,
            itemTransactionListBinding: ItemTransactionListBinding
        ) {
            if(transaction.isTransactionRejected()){
                if(transaction.productCode == TransactionProductCode.POS_PURCHASE.pCode ||
                    transaction.productCode == TransactionProductCode.TOP_UP_VIA_CARD.pCode ||
                    transaction.productCode == TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode) {
                    itemTransactionListBinding.ivTransaction.setImageResource(R.drawable.ic_reverted)
                } else {
                    ImageBinding.loadAvatar(
                        itemTransactionListBinding.ivTransaction,
                        if (TxnType.valueOf(
                                transaction.txnType ?: ""
                            ) == TxnType.DEBIT
                        ) transaction.receiverProfilePictureUrl else transaction.senderProfilePictureUrl,
                        if (transaction.txnType == TxnType.DEBIT.type) transaction.receiverName else transaction.senderName,
                        android.R.color.transparent,
                        R.dimen.text_size_h2
                    )
                    itemTransactionListBinding.ivTransaction.alpha = 0.5f
                }
            }else {
                ImageBinding.loadAvatar(
                    itemTransactionListBinding.ivTransaction,
                    if (TxnType.valueOf(
                            transaction.txnType ?: ""
                        ) == TxnType.DEBIT
                    ) transaction.receiverProfilePictureUrl else transaction.senderProfilePictureUrl,
                    if (transaction.txnType == TxnType.DEBIT.type) transaction.receiverName else transaction.senderName,
                    android.R.color.transparent,
                    R.dimen.text_size_h2
                )
            }
        }


        private fun setInitialsAsTxnImage(
            transaction: Transaction,
            itemTransactionListBinding: ItemTransactionListBinding
        ) {
            ImageBinding.loadAvatar(
                itemTransactionListBinding.ivTransaction,
                "",
                transaction.title,
                android.R.color.transparent,
                R.dimen.text_size_h2
            )
        }

        private fun setContentDataColor(
            transaction: Transaction,
            itemTransactionListBinding: ItemTransactionListBinding
        ) {

            val context = itemTransactionListBinding.ivIncoming.context
            val isTxnCancelled = transaction.isTransactionCancelled()
            itemTransactionListBinding.tvTransactionName.setTextColor(context.getColors(if (isTxnCancelled) R.color.greyNormalDark else R.color.colorMidnightExpress))
            itemTransactionListBinding.tvTransactionTimeAndCategory.setTextColor(
                context.getColors(
                    if (isTxnCancelled) R.color.greyNormalDark else R.color.greyDark
                )
            )
            itemTransactionListBinding.tvTransactionAmount.setTextColor(
                context.getColors(transaction.getTransactionAmountColor())
            )
            itemTransactionListBinding.tvCurrency.setTextColor(context.getColors(if (isTxnCancelled) R.color.greyNormalDark else R.color.greyDark))

            //strike-thru textview
            itemTransactionListBinding.tvTransactionAmount.paintFlags =
                if (transaction.isTransactionCancelled() || transaction.status == TransactionStatus.FAILED.name) itemTransactionListBinding.tvTransactionAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else 0
        }
    }
}

