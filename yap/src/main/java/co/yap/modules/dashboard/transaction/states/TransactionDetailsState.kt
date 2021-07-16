package co.yap.modules.dashboard.transaction.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.transaction.interfaces.ITransactionDetails
import co.yap.yapcore.BaseState

class TransactionDetailsState : BaseState(), ITransactionDetails.State {

    override var spentVisibility: ObservableField<Boolean> = ObservableField(false)
    @get:Bindable
    override var transactionNoteDate: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transactionNoteDate)
        }

    override var txnNoteValue: ObservableField<String> = ObservableField()
    override var isTransferTxn: ObservableField<Boolean> = ObservableField(false)
    override var categoryTitle: ObservableField<String> = ObservableField("")
    override var categoryIcon: ObservableField<Int> = ObservableField()
    override var transactionTitle: ObservableField<String> = ObservableField()


}