package co.yap.networking.store

import co.yap.networking.models.ApiResponse
import co.yap.networking.store.requestdtos.CreateStoreRequest
import co.yap.networking.store.responsedtos.StoreParent
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StoresRetroService {

    @GET(StoresRepository.URL_GET_STORES)
    suspend fun getStores(): Response<StoreParent>
}