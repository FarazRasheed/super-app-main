package co.yap.networking.transactions.responsedtos.transaction

import com.google.gson.annotations.SerializedName

data class TransactionAnalyticsDetailsResponse(
    @SerializedName("averageSpending")
    val averageSpending: Double? = null,
    @SerializedName("currentToLastMonth")
    val currentToLastMonth: Double? = null,
    @SerializedName("transactionDetails")
    val txnAnalytics: ArrayList<Transaction>? = null
)