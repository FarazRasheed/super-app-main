package co.yap.modules.dashboard.cards.paymentcarddetail.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.home.filters.models.TransactionFilters
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.cards.responsedtos.CardDetail
import co.yap.networking.transactions.requestdtos.CardTransactionRequest
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IPaymentCardDetail {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val EVENT_FREEZE_UNFREEZE_CARD: Int get() = 1
        val EVENT_CARD_DETAILS: Int get() = 2
        val EVENT_REMOVE_CARD: Int get() = 3
        val clickEvent: SingleClickEvent
        var card: MutableLiveData<Card>
        var cardDetail: CardDetail
        fun handlePressOnView(id: Int)
        fun getCardBalance(updatedBalance: (balance:String) -> Unit)
        fun freezeUnfreezeCard()
        fun getCardDetails()
        fun removeCard()
        //
        val cardTransactionRequest: CardTransactionRequest
        val EVENT_SET_CARD_PIN: Int get() = 1
        val EVENT_SET_COMPLETE_VEERIFICATION: Int get() = 2
        var MAX_CLOSING_BALANCE: Double
        var debitCardSerialNumber: String
        fun getDebitCards()
        fun requestAccountTransactions()
        val transactionsLiveData: MutableLiveData<List<HomeTransactionListData>>
        val isLoadMore: MutableLiveData<Boolean>
        val isLast: MutableLiveData<Boolean>
        fun loadMore()
        var transactionFilters: TransactionFilters
    }

    interface State : IBase.State {
        var accountType: String
        var cardType: String
        var cardTypeText: String
        var cardPanNumber: String
        var cardBalance: String
        var cardName: String
        var blocked: Boolean
        var physical: Boolean
        var balanceLoading: Boolean
        var filterCount: ObservableField<Int>
        var cardStatus: ObservableField<String>
        var isTxnsEmpty: ObservableField<Boolean>
    }
}