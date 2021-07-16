package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class TransactionThresholdResponseDTO(
    @SerializedName("data")
    val data: TransactionThresholdModel
) : ApiResponse()