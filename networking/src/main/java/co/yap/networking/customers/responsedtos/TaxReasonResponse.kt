package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class TaxReasonResponse(
    @SerializedName("data")
    var reasons: ArrayList<String> = arrayListOf(),
    @SerializedName("errors")
    var errors: Any
) : ApiResponse()