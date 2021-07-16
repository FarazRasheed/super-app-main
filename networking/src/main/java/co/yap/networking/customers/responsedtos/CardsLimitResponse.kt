package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CardsLimitResponse(
    @SerializedName("data")
    var cardLimits: CardLimits? = null,
    @SerializedName("errors")
    var errors: Any? = null
): ApiResponse()