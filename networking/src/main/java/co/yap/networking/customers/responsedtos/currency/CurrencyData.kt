package co.yap.networking.customers.responsedtos.currency

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("code")
    var currencyCode: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("isoNum")
    var isoNum: String? = null,
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("allowedDecimalsNumber")
    var allowedDecimalsNumber: String? = "",
    @SerializedName("default")
    var default: String? = null
) : ApiResponse()