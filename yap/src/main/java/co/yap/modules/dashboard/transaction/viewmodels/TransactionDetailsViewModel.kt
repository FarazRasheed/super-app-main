package co.yap.modules.dashboard.transaction.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import co.yap.modules.dashboard.transaction.interfaces.ITransactionDetails
import co.yap.modules.dashboard.transaction.states.TransactionDetailsState
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.DateUtils.FORMAT_LONG_OUTPUT
import co.yap.yapcore.helpers.extentions.getCategoryIcon
import co.yap.yapcore.helpers.extentions.getCategoryTitle
import co.yap.yapcore.helpers.extentions.getFormattedTime
import co.yap.yapcore.helpers.extentions.getTransactionNoteDate


class TransactionDetailsViewModel(application: Application) :
    BaseViewModel<ITransactionDetails.State>(application), ITransactionDetails.ViewModel {

    override val state: TransactionDetailsState = TransactionDetailsState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var transaction: ObservableField<Transaction> = ObservableField()
    override fun onCreate() {
        super.onCreate()
        setStatesData()
    }

    override fun handlePressOnEditNoteClickEvent(id: Int) {
        clickEvent.postValue(id)
    }

    private fun setStatesData() {
        transaction.get()?.let { transaction ->
            setToolbarTitle()
            setTransactionNoteDate()
            state.txnNoteValue.set(transaction.transactionNote)
            setSenderOrReceiver(transaction)
            state.categoryTitle.set(transaction.getCategoryTitle())
            state.categoryIcon.set(transaction.getCategoryIcon())
        }
    }

    private fun setToolbarTitle() {
        state.toolbarTitle = transaction.get().getFormattedTime(FORMAT_LONG_OUTPUT)
    }

    private fun setTransactionNoteDate() {
        if (transaction.get().getTransactionNoteDate(FORMAT_LONG_OUTPUT).isEmpty()) {
            state.transactionNoteDate =
                state.editNotePrefixText + transaction.get()?.transactionNoteDate
        } else {
            state.transactionNoteDate =
                state.editNotePrefixText + transaction.get()
                    .getTransactionNoteDate(FORMAT_LONG_OUTPUT)
        }
    }

    private fun setSenderOrReceiver(transaction: Transaction) {
        when (transaction.productCode ?: "") {
            TransactionProductCode.Y2Y_TRANSFER.pCode, TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.CASH_PAYOUT.pCode -> {
                state.isTransferTxn.set(true)
            }
        }
    }

}