package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class AddFundsRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("toCard")
    val toCard: String
)
