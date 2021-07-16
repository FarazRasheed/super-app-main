package co.yap.networking.store

import co.yap.networking.BaseRepository
import co.yap.networking.RetroNetwork
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.store.requestdtos.CreateStoreRequest
import co.yap.networking.store.responsedtos.StoreParent

object StoresRepository : BaseRepository(), StoresApi {

    const val URL_GET_STORES = "/v2/everything?q=sports&apiKey=aa67d8d98c8e4ad1b4f16dbd5f3be348"
    private val API: StoresRetroService = RetroNetwork.createService(StoresRetroService::class.java)

    override suspend fun getYapStores(createStoreRequest: CreateStoreRequest): RetroApiResponse<StoreParent> =
        MessagesRepository.executeSafely(call = { API.getStores() })

}