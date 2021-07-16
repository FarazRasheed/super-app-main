package co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IAddFunds {
    interface View : IBase.View<ViewModel> {
        fun addObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handleOnPressView(id: Int)
        fun getFundTransferDenominations()
        fun getFundTransferLimits()
        fun getTransactionThresholds()
        fun addFunds(success:()->Unit)
        val clickEvent: SingleClickEvent
        var transactionThreshold: TransactionThresholdModel?
        var errorDescription: String
    }

    interface State : IBase.State {
        var card: ObservableField<Card>
        var amount: String
        var firstDenomination: ObservableField<String>
        var secondDenomination: ObservableField<String>
        var thirdDenomination: ObservableField<String>
        var transferFee: ObservableField<CharSequence>
        var availableBalance: ObservableField<CharSequence>
        var topUpSuccessMsg: ObservableField<CharSequence>
        var debitCardUpdatedBalance: ObservableField<CharSequence>
        var spareCardUpdatedBalance: ObservableField<CharSequence>
        var maxLimit: Double
        var minLimit: Double
        var valid: ObservableBoolean
    }
}