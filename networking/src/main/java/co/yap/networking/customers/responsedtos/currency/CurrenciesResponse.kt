package co.yap.networking.customers.responsedtos.currency

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("data")
    var curriencies: ArrayList<CurrencyData>? = null
) : ApiResponse()