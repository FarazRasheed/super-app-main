package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import co.yap.networking.transactions.responsedtos.transaction.TransactionAnalyticsDetailsResponse
import com.google.gson.annotations.SerializedName

data class AnalyticsDetailResponseDTO(
    @SerializedName("data")
    val data: TransactionAnalyticsDetailsResponse? = null
) : ApiResponse()
