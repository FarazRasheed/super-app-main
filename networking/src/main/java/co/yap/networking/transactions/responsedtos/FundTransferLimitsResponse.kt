package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class FundTransferLimitsResponse(
    @SerializedName("data")
    val data: FundTransferLimits? = null
) : ApiResponse()