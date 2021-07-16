package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class ChangePasscodeRequest(
    @SerializedName("new-password")
    val newPassword: String,
    @SerializedName("token")
    val token: String
)