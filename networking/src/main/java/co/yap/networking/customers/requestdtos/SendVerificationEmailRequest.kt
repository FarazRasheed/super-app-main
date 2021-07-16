package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class SendVerificationEmailRequest(
    @SerializedName("email") val email: String,
    @SerializedName("accountType") val accountType: String,
    @SerializedName("token") val token: String? = null
)
