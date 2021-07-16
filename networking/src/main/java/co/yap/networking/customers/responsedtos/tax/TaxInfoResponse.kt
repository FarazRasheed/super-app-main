package co.yap.networking.customers.responsedtos.tax

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class TaxInfoResponse(
    @SerializedName("data")
    var pdf: String? = null,
    @SerializedName("errors")
    var errors: Any? = null
): ApiResponse()