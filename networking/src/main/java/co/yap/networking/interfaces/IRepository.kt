package co.yap.networking.interfaces

import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse
import retrofit2.Response

internal interface IRepository {
    suspend fun <T : ApiResponse> executeSafely(call: suspend () -> Response<T>): RetroApiResponse<T>
}