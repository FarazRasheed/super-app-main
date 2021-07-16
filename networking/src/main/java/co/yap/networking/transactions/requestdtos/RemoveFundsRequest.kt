package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class RemoveFundsRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("fromCard")
    val fromCard: String
)
