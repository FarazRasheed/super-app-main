package co.yap.networking.customers.responsedtos.beneficiary

import com.google.gson.annotations.SerializedName

data class TopUpTransactionModel(
    @SerializedName("orderId") var orderId: String?,
    @SerializedName("currency") var currency: String? = "",
    @SerializedName("amount") var amount: String? = "",
    @SerializedName("cardId") var cardId: Int?,
    @SerializedName("secureId") var secureId: String?
) {
}