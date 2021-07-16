package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

class AddRemoveFunds(@SerializedName("transactionId") val transactionId: String, @SerializedName("balance") val balance: String)