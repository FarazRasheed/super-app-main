package co.yap.modules.dashboard.store.paging.transactions

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import co.yap.modules.dashboard.home.viewmodels.YapHomeViewModel
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.HomeTransactionsRequest
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionsResponse
import co.yap.yapcore.helpers.PagingState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class TransactionsDataSource(
    private val storeRepo: TransactionsRepository,
    private val yapHomeViewModel: YapHomeViewModel
) :
    PageKeyedDataSource<Int, HomeTransactionListData>() {

    var state: MutableLiveData<PagingState> = MutableLiveData()

    var homeTransactionsRequest: HomeTransactionsRequest =
        HomeTransactionsRequest(
            1,
            6,
            0.00,
            20000.00,
            null,
            null,
            totalAppliedFilter = 0
        )

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, HomeTransactionListData>
    ) {

        updateState(PagingState.LOADING)
        homeTransactionsRequest.number = 1
        homeTransactionsRequest.size = params.requestedLoadSize

        GlobalScope.launch {
            when (val response =
                storeRepo.getAccountTransactions(homeTransactionsRequest)) {
                is RetroApiResponse.Success -> {

                    var transactionModelData: ArrayList<HomeTransactionListData> =
                        setUpSectionHeader(response)
                    if (response.data.data.pageable.offset < response.data.data.totalElements) {
                        homeTransactionsRequest.number = +1
                        callback.onResult(
                            transactionModelData,
                            response.data.data.pageable.pageNumber - 1,
                            response.data.data.pageable.pageNumber + 1
                        )
                        updateState(PagingState.DONE)
                    } else {
                        updateState(PagingState.LOADING)
                    }
                }
                is RetroApiResponse.Error -> {
                    updateState(PagingState.ERROR)
                }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, HomeTransactionListData>
    ) {
        updateState(PagingState.LOADING)
        homeTransactionsRequest.number = params.key
        homeTransactionsRequest.size = params.requestedLoadSize
        GlobalScope.launch {
            when (val response =
                storeRepo.getAccountTransactions(homeTransactionsRequest)) {
                is RetroApiResponse.Success -> {
                    var transactionModelData: ArrayList<HomeTransactionListData> =
                        setUpSectionHeader(response)
                    homeTransactionsRequest.number = +1
                    callback.onResult(
                        transactionModelData,
                        if (response.data.data.last) null else params.key + 1
                    )
                    updateState(PagingState.DONE)

                }
                is RetroApiResponse.Error -> {
                    callback.onResult(listOf(), homeTransactionsRequest.number)
                    updateState(PagingState.ERROR)
                }
            }
        }
    }

    private fun setUpSectionHeader(response: RetroApiResponse.Success<HomeTransactionsResponse>): ArrayList<HomeTransactionListData> {
//        yapHomeViewModel.contentList = response.dataList.dataList.content as ArrayList<Content>
//        Collections.sort(yapHomeViewModel.contentList, object :
//            Comparator<Content> {
//            override fun compare(
//                o1: Content,
//                o2: Content
//            ): Int {
//                return o2.creationDate!!.compareTo(o1.creationDate!!)
//            }
//        })
//
//        val groupByDate = yapHomeViewModel.contentList.groupBy { item ->
//            yapHomeViewModel.convertDate(item.creationDate!!)
//        }
//
////        println(groupByDate.entries.joinToString(""))
//
//        var transactionModelData: ArrayList<HomeTransactionListData> =
//            arrayListOf()
//
//        for (transactionsDay in groupByDate.entries) {
//
//
//            var contentsList: ArrayList<Content> = arrayListOf()
//            println(transactionsDay.key)
//            println(transactionsDay.value)
//            contentsList = transactionsDay.value as ArrayList<Content>
//            contentsList.sortByDescending { it ->
//                it.creationDate
//            }
//
//            var closingBalanceOfTheDay: Double = contentsList.get(0).balanceAfter
//            yapHomeViewModel.closingBalanceArray.add(closingBalanceOfTheDay)
//
//            var transactionModel: HomeTransactionListData = HomeTransactionListData(
//                "Type",
//                "AED",
//                transactionsDay.key!!,
//                contentsList.get(0).totalAmount.toString(),
//                contentsList.get(0).balanceAfter,
//                0.00 /*  "calculate the percentage as per formula from the keys".toDouble()*/,
//                contentsList,
//
//                response.dataList.dataList.first,
//                response.dataList.dataList.last,
//                response.dataList.dataList.number,
//                response.dataList.dataList.numberOfElements,
//                response.dataList.dataList.pageable,
//                response.dataList.dataList.size,
//                response.dataList.dataList.sort,
//                response.dataList.dataList.totalElements,
//                response.dataList.dataList.totalPages
//            )
//            transactionModelData.add(transactionModel)
//
//            yapHomeViewModel.transactionLogicHelper.transactionList =
//                transactionModelData
//            yapHomeViewModel.MAX_CLOSING_BALANCE =
//                yapHomeViewModel.closingBalanceArray.max()!!
//        }
        return arrayListOf()
    }


    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, HomeTransactionListData>
    ) {
    }

    private fun updateState(state: PagingState) {
        this.state.postValue(state)
    }

    fun retry() {

    }
}