package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class CardStatementsResponse(
    @SerializedName("data")
    val data: List<CardStatement>? = null
) : ApiResponse()