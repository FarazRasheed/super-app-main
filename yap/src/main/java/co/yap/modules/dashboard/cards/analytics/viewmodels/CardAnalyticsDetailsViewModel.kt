package co.yap.modules.dashboard.cards.analytics.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.cards.analytics.interfaces.ICardAnalyticsDetails
import co.yap.modules.dashboard.cards.analytics.main.viewmodels.CardAnalyticsBaseViewModel
import co.yap.modules.dashboard.cards.analytics.states.CardAnalyticsDetailsState
import co.yap.modules.dashboard.home.adaptor.TransactionsListingAdapter
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.networking.transactions.responsedtos.transaction.TransactionAnalyticsDetailsResponse
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.managers.SessionManager

class CardAnalyticsDetailsViewModel(application: Application) :
    CardAnalyticsBaseViewModel<ICardAnalyticsDetails.State>(application),
    ICardAnalyticsDetails.ViewModel {
    override val state = CardAnalyticsDetailsState()
    override val adapter: TransactionsListingAdapter = TransactionsListingAdapter(arrayListOf())
    override var transactionResponse: TransactionAnalyticsDetailsResponse =
        TransactionAnalyticsDetailsResponse()
    val repository: TransactionsRepository = TransactionsRepository
    var list: MutableList<Transaction> = arrayListOf()
    var viewState: MutableLiveData<Int> = MutableLiveData(Constants.EVENT_LOADING)

    override var clickEvent: SingleClickEvent? = SingleClickEvent()

    override fun handleOnClickEvent(id: Int) {
        clickEvent?.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        fetchMerchantTransactions(
            Constants.MERCHANT_TYPE,
            parentViewModel?.state?.currentSelectedDate ?: ""
        )
        setToolBarTitle(state.title.get()?.trim() ?: "Analytics")
    }

    override fun fetchMerchantTransactions(merchantType: String, currentDate: String) {
        launch {
            state.loading = true
            when (val response = repository.getTransactionsOfMerchant(
                merchantType,
                SessionManager.getCardSerialNumber(),
                parentViewModel?.state?.currentSelectedDate,
                state.categories
            )) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let { resp ->
                        transactionResponse = resp
                        state.avgSpending.set("${transactionResponse.averageSpending}")
                        state.currToLast.set("${transactionResponse.currentToLastMonth}%")
                        if (!transactionResponse.txnAnalytics.isNullOrEmpty()) {
                            viewState.value = Constants.EVENT_CONTENT
                            list =transactionResponse.txnAnalytics?: arrayListOf()
                            list.let { transactionList ->
                                adapter.setList(transactionList)
                            }
                        } else viewState.value = Constants.EVENT_EMPTY
                        state.loading = false
                    }
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                }
            }
        }
    }
}
