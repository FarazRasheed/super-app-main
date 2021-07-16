package co.yap.networking.customers.responsedtos.currency

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CurrenciesByCodeResponse(
    @SerializedName("data")
    var curriency: CurrencyData? = null
) : ApiResponse()