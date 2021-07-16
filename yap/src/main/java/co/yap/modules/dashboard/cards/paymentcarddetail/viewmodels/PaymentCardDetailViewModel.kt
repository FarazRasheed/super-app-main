package co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels

import android.app.Application
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IPaymentCardDetail
import co.yap.modules.dashboard.cards.paymentcarddetail.states.PaymentCardDetailState
import co.yap.modules.dashboard.home.filters.models.TransactionFilters
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.requestdtos.CardLimitConfigRequest
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.cards.responsedtos.CardBalance
import co.yap.networking.cards.responsedtos.CardDetail
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.CardTransactionRequest
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionsResponse
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.CardStatus
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class PaymentCardDetailViewModel(application: Application) :
    BaseViewModel<IPaymentCardDetail.State>(application),
    IPaymentCardDetail.ViewModel {

    override val state: PaymentCardDetailState = PaymentCardDetailState()
    private val cardsRepository: CardsRepository = CardsRepository
    override var card: MutableLiveData<Card> = MutableLiveData()
    override lateinit var cardDetail: CardDetail
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override var MAX_CLOSING_BALANCE: Double = 0.0
    private var closingBalanceArray: ArrayList<Double> = arrayListOf()
    override lateinit var debitCardSerialNumber: String
    private val transactionsRepository: TransactionsRepository = TransactionsRepository
    override val transactionsLiveData: MutableLiveData<List<HomeTransactionListData>> =
        MutableLiveData()
    override val isLoadMore: MutableLiveData<Boolean> = MutableLiveData(false)
    override val isLast: MutableLiveData<Boolean> = MutableLiveData(false)
    override var transactionFilters: TransactionFilters = TransactionFilters()

    private var sortedCombinedTransactionList: ArrayList<HomeTransactionListData> = arrayListOf()

    override var cardTransactionRequest: CardTransactionRequest =
        CardTransactionRequest(0, 20, "", null, null)

    override fun requestAccountTransactions() {
        launch {
            if (!isLoadMore.value!!)
                state.loading = true
            when (val response =
                transactionsRepository.getCardTransactions(cardTransactionRequest)) {
                is RetroApiResponse.Success -> {
                    isLast.value = response.data.data.last
                    val transactionModelData: ArrayList<HomeTransactionListData> =
                        setUpSectionHeader(response)

                    if (false /*isRefreshing.value!!*/) {
                        sortedCombinedTransactionList.clear()
                    }
                    /*isRefreshing.value!! -->  isRefreshing.value = false*/

                    if (!sortedCombinedTransactionList.equals(transactionModelData)) {
                        sortedCombinedTransactionList.addAll(transactionModelData)
                    }

                    val unionList =
                        (sortedCombinedTransactionList.asSequence() + transactionModelData.asSequence())
                            .distinct()
                            .groupBy { it.date }

                    for (lists in unionList.entries) {
                        if (lists.value.size > 1) {// sortedCombinedTransactionList.equals(transactionModelData fails in this case
                            var contentsList: ArrayList<Transaction> = arrayListOf()

                            for (transactionsDay in lists.value) {
                                contentsList.addAll(transactionsDay.transaction)

                            }

                            contentsList.sortByDescending { it ->
                                it.creationDate
                            }


                            var closingBalanceOfTheDay = contentsList[0].balanceAfter ?: 0.0
                            closingBalanceArray.add(closingBalanceOfTheDay)

                            var transactionModel: HomeTransactionListData = HomeTransactionListData(
                                "Type",
                                SessionManager.getDefaultCurrency(),
                                /* transactionsDay.key!!*/
                                convertDate(contentsList[0].creationDate),
                                contentsList[0].totalAmount.toString(),
                                contentsList[0].balanceAfter,
                                0.00 /*  "calculate the percentage as per formula from the keys".toDouble()*/,
                                contentsList,

                                response.data.data.first,
                                response.data.data.last,
                                response.data.data.number,
                                response.data.data.numberOfElements,
                                response.data.data.pageable,
                                response.data.data.size,
                                response.data.data.sort,
                                response.data.data.totalElements,
                                response.data.data.totalPages
                            )
                            var numberstoReplace: Int = 0
                            var replaceNow: Boolean = false


                            val iterator = sortedCombinedTransactionList.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                if (item.date == convertDate(contentsList[0].creationDate)) {
                                    numberstoReplace = sortedCombinedTransactionList.indexOf(item)
                                    iterator.remove()
                                    replaceNow = true

                                }

                            }
                            if (replaceNow) {
                                sortedCombinedTransactionList.add(
                                    numberstoReplace,
                                    transactionModel
                                )
                                replaceNow = false
                            }
                        }
                    }
//                    sortedCombinedTransactionList.sortBy { it ->  it.date  }

                    transactionsLiveData.value = sortedCombinedTransactionList
                    //if (isLoadMore.value!!)
                    isLoadMore.value = false
                    //transactionLogicHelper.transactionList = sortedCombinedTransactionList
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    /*/isRefreshing.value = false*/
                    isLoadMore.value = false
                }
            }
        }
        state.loading = false
    }

    private fun setUpSectionHeader(response: RetroApiResponse.Success<HomeTransactionsResponse>): ArrayList<HomeTransactionListData> {
        val contentList = response.data.data.transaction as ArrayList<Transaction>
        contentList.sortWith(Comparator { o1, o2 ->
            o2.creationDate?.compareTo(o1?.creationDate!!)!!
        })
        val groupByDate = contentList.groupBy { item ->
            convertDate(item.creationDate)
        }

        val transactionModelData: ArrayList<HomeTransactionListData> =
            arrayListOf()

        for (transactionsDay in groupByDate.entries) {

            val contentsList = transactionsDay.value as ArrayList<Transaction>
            contentsList.sortByDescending {
                it.creationDate
            }

            val closingBalanceOfTheDay = contentsList[0].balanceAfter ?: 0.0
            closingBalanceArray.add(closingBalanceOfTheDay)

            val transactionModel = HomeTransactionListData(
                "Type",
                SessionManager.getDefaultCurrency(),
                transactionsDay.key!!,
                contentsList[0].totalAmount.toString(),
                contentsList[0].balanceAfter,
                0.00 /*  "calculate the percentage as per formula from the keys".toDouble()*/,
                contentsList,

                response.data.data.first,
                response.data.data.last,
                response.data.data.number,
                response.data.data.numberOfElements,
                response.data.data.pageable,
                response.data.data.size,
                response.data.data.sort,
                response.data.data.totalElements,
                response.data.data.totalPages
            )
            transactionModelData.add(transactionModel)
            MAX_CLOSING_BALANCE =
                closingBalanceArray.max()!!
        }
        return transactionModelData
    }

    override fun loadMore() {
        requestAccountTransactions()
    }

    override fun handlePressOnView(id: Int) {
        if (!isBlockedAction(id))
            clickEvent.setValue(id)
    }

    private fun isBlockedAction(id: Int): Boolean {
        return if (card.value?.status == CardStatus.EXPIRED.name) {
            when (id) {
                R.id.llFreezeSpareCard, R.id.llFreezePrimaryCard, R.id.llCardLimits -> true
                else -> false
            }
        } else
            false
    }

    override fun onResume() {
        super.onResume()
        cardTransactionRequest.number = 0
        sortedCombinedTransactionList.clear()
    }

    override fun getCardBalance(updatedBalance: (balance: String) -> Unit) {
        launch {
            state.balanceLoading = true
            when (val response = cardsRepository.getCardBalance(card.value?.cardSerialNumber!!)) {
                is RetroApiResponse.Success -> {
                    try {
                        val cardBalance: CardBalance? = response.data.data?.let {
                            it
                        }
                        updatedBalance(cardBalance?.availableBalance ?: "0.0")
                        card.value?.availableBalance = cardBalance?.availableBalance.toString()
                        state.cardBalance = cardBalance?.availableBalance?.toFormattedCurrency(
                            showCurrency = true,
                            currency = cardBalance.currencyCode ?: SessionManager.getDefaultCurrency()
                        ) ?: ""
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
            state.balanceLoading = false
        }
    }

    override fun freezeUnfreezeCard() {
        launch {
            state.loading = true
            when (val response =
                cardsRepository.freezeUnfreezeCard(CardLimitConfigRequest(card.value?.cardSerialNumber!!))) {
                is RetroApiResponse.Success -> {
                    Handler().postDelayed({
                        state.loading = false
                        clickEvent.setValue(EVENT_FREEZE_UNFREEZE_CARD)
                    }, 400)

                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }

        }
    }

    override fun getCardDetails() {
        launch {
            state.loading = true
            when (val response = cardsRepository.getCardDetails(card.value?.cardSerialNumber!!)) {
                is RetroApiResponse.Success -> {
                    cardDetail = response.data.data
                    clickEvent.setValue(EVENT_CARD_DETAILS)
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
            state.loading = false
        }
    }

    override fun removeCard() {
        launch {
            state.loading = true
            when (val response =
                cardsRepository.removeCard(
                    CardLimitConfigRequest(
                        card.value?.cardSerialNumber ?: ""
                    )
                )) {
                is RetroApiResponse.Success -> {
                    clickEvent.setValue(EVENT_REMOVE_CARD)
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
            state.loading = false
        }
    }

    override fun getDebitCards() {
        launch {
            state.loading = true
            when (val response = cardsRepository.getDebitCards("DEBIT")) {
                is RetroApiResponse.Success -> {
                    if (response.data.data?.size != 0) {
                        response.data.data?.let {
                            debitCardSerialNumber = it[0].cardSerialNumber
                        }
                        clickEvent.setValue(EVENT_SET_CARD_PIN)
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
            state.loading = false
        }

    }

    private fun convertDate(creationDate: String?): String? {
        creationDate?.let {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val convertedDate = parser.parse(creationDate)
            parser.timeZone = TimeZone.getDefault()
            val pattern = "MMMM dd, yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getDefault()
            return simpleDateFormat.format(convertedDate)
        }
        return ""
    }

}