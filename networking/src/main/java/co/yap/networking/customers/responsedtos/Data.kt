package co.yap.networking.customers.responsedtos

import com.google.gson.annotations.SerializedName

data class CardLimits(
    @SerializedName("currentCount") var currentCount: Int,
    @SerializedName("maxLimit") var maxLimit: Int,
    @SerializedName("remaining") var remaining: Int
)