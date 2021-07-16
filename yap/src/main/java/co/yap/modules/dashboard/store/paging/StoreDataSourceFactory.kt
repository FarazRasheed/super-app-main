package co.yap.modules.dashboard.store.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.yap.networking.store.StoresRepository
import co.yap.networking.store.responsedtos.Store

class StoreDataSourceFactory(private val storeRepo: StoresRepository) :
    DataSource.Factory<Long, Store>() {

    val storeDataSourceLiveData = MutableLiveData<StoreDataSource>()

    override fun create(): DataSource<Long, Store> {
        val storeDataSource = StoreDataSource(storeRepo)
        storeDataSourceLiveData.postValue(storeDataSource)
        return storeDataSource
    }
}