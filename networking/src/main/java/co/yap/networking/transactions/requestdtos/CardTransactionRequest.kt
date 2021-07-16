package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class CardTransactionRequest(
    @SerializedName("number")
    var number: Int,
    @SerializedName("size")
    var size: Int,
    @SerializedName("serialNumber")
    var serialNumber: String,
    @SerializedName("amountStartRange")
    var amountStartRange: Double? = 0.0,
    @SerializedName("amountEndRange")
    var amountEndRange: Double? = 0.0,
    @SerializedName("txnType")
    var txnType: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("totalAppliedFilter")
    var totalAppliedFilter: Int = 0
)
