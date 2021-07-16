package co.yap.modules.dashboard.cards.analytics.interfaces

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.home.adaptor.TransactionsListingAdapter
import co.yap.networking.transactions.responsedtos.transaction.TransactionAnalyticsDetailsResponse
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICardAnalyticsDetails {
    interface View : IBase.View<ViewModel> {
    }
    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent?
        fun handleOnClickEvent(id: Int)
        val adapter: TransactionsListingAdapter
        var transactionResponse : TransactionAnalyticsDetailsResponse
        fun fetchMerchantTransactions(merchantType : String, currentDate : String)
    }
    interface State : IBase.State {
        var title : ObservableField<String>
        var totalSpendings : ObservableField<String>
        var countWithDate : ObservableField<String>
        var avgSpending : ObservableField<String>
        var currToLast : ObservableField<String>
        var monthlyTotalPercentage : ObservableField<String>
        var ImageUrl : ObservableField<String>
        var position : Int
        var percentCardVisibility : Boolean
        var categories : ArrayList<String>?
    }
}