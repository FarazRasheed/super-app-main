package co.yap.modules.dashboard.transaction.interfaces

import androidx.databinding.ObservableField
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITransactionDetails {
    interface View : IBase.View<ViewModel> {
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnEditNoteClickEvent(id: Int)
        var clickEvent: SingleClickEvent
        var transaction: ObservableField<Transaction>
    }

    interface State : IBase.State {
        var txnNoteValue: ObservableField<String>
        var isTransferTxn: ObservableField<Boolean>
        var spentVisibility: ObservableField<Boolean>
        var categoryTitle: ObservableField<String>
        var categoryIcon: ObservableField<Int>
        var transactionTitle: ObservableField<String>
        var transactionNoteDate: String?
        val editNotePrefixText: String get() = "Note added "
    }
}