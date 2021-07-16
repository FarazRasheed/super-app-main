package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class HouseHoldCardsDesignResponse(
    @SerializedName("data") val data: List<HouseHoldCardsDesign>? = null
) : ApiResponse()
