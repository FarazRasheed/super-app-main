package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class FundTransferDenominationsResponse(
    @SerializedName("data")
    val data: List<FundTransferDenominations>? = null
) : ApiResponse()