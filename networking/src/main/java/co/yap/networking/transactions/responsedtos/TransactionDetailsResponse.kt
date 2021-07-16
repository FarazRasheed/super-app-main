package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class TransactionDetailsResponse(
    @SerializedName("data")
    val data: TransactionDetails? = null
) : ApiResponse()