package co.yap.modules.dashboard.store.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import co.yap.R
import co.yap.networking.store.StoresRepository
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.helpers.PagingState

class StoreDataSource(private val storeRepo: StoresRepository) :
    PageKeyedDataSource<Long, Store>() {

    var state: MutableLiveData<PagingState> = MutableLiveData()

    fun retry() {

    }

    private fun setRetry() {

    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Store>
    ) {
//        updateState(PagingState.LOADING)
//        GlobalScope.launch {
//            when (val response =
//                storeRepo.getYapStores(CreateStoreRequest())) {
//                is RetroApiResponse.Success -> {
//                    callback.onResult(
//                        response.dataList.stores,
//                        null,
//                        2
//                    )
//                    updateState(PagingState.DONE)
//                }
//                is RetroApiResponse.Error -> {
//                    callback.onResult(
//                        getDummyList().take(2),
//                        null,
//                        null
//                    )
//                    updateState(PagingState.DONE)
//
//                    //setRetry(Action { loadInitial(params, callback) })
//                    //callback.onResult(listOf(), 111L, 1221L)
//                    //updateState(PagingState.ERROR)
//                }
//            }
//        }

        callback.onResult(
            getDummyList().take(2),
            null,
            null
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Store>) {
//        updateState(PagingState.LOADING)
//        GlobalScope.launch {
//            when (val response =
//                storeRepo.getYapStores(CreateStoreRequest())) {
//                is RetroApiResponse.Success -> {
//                    callback.onResult(
//                        response.dataList.stores,
//                        params.key + 1
//                    )
//                    updateState(PagingState.DONE)
//                }
//                is RetroApiResponse.Error -> {
//                    callback.onResult(listOf(), 111L)
//                    updateState(PagingState.ERROR)
//                }
//            }
//        }
    }

    //Assume sample response
    fun getDummyList(): MutableList<Store> {
        return mutableListOf(
            Store(
                1,
                "YAP Young",
                "Open a bank account for your children and help empower them financially.",
                R.drawable.ic_store_young, R.drawable.ic_young_smile
            )
            ,
            Store(
                2,
                "YAP Household",
                "Manage your household salaries digitally.",
                R.drawable.ic_store_household, R.drawable.ic_young_household
            ),
            Store(
                3,
                "YAP B2B",
                "Manage your household salaries digitally.",
                R.drawable.ic_store_b2b, R.drawable.ic_young_smile
            ),
            Store(
                4,
                "YAP B2C",
                "Manage your household salaries digitally.",
                R.drawable.ic_store_b2c, R.drawable.ic_young_smile
            ),
            Store(
                5,
                "Financial Freedom for All",
                "Yap’s mission is to enable everyone with financial freedom. Financial fr…",
                R.drawable.ic_freedom, R.drawable.ic_young_smile
            )
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Store>) {
    }

    private fun updateState(state: PagingState) {
        this.state.postValue(state)
    }
}