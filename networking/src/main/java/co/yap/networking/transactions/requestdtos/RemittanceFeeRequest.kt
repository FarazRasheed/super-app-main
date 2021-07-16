package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class RemittanceFeeRequest(
    @SerializedName("country") val country: String? = null,
    @SerializedName("currency") val currency: String? = null
)