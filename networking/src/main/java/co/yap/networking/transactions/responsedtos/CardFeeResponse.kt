package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class CardFeeResponse(
    @SerializedName("data")
    val data: CardFee? = null
) : ApiResponse()