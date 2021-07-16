package co.yap.networking.customers.responsedtos

import co.yap.networking.customers.responsedtos.AppUpdate
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class AppUpdateResponse(
    @SerializedName("data")
    val data: List<AppUpdate>? = arrayListOf()
): ApiResponse()