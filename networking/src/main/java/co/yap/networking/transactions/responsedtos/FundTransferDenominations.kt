package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

class FundTransferDenominations(@SerializedName("amount") val amount: String, @SerializedName("active") val active: Boolean)