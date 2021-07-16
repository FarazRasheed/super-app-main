package co.yap.networking.store

import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.store.requestdtos.CreateStoreRequest
import co.yap.networking.store.responsedtos.StoreParent

interface StoresApi {
    suspend fun getYapStores(createStoreRequest: CreateStoreRequest): RetroApiResponse<StoreParent>
}