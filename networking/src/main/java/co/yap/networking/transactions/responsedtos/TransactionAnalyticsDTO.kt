package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

data class TransactionAnalyticsDTO(
    @SerializedName("totalTxnCount")
    val totalTxnCount: Int? = null,
    @SerializedName("totalTxnAmount")
    val totalTxnAmount: Double? = null,
    @SerializedName("monthlyAvgAmount")
    val monthlyAvgAmount: Double? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("txnAnalytics")
    val txnAnalytics: ArrayList<TxnAnalytic>? = null
)