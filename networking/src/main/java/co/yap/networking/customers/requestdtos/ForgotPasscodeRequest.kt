package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class ForgotPasscodeRequest(
    @SerializedName("mobileNo") val mobileNo: String,
    @SerializedName("newPassword") val newPassword: String,
    @SerializedName("token") val token: String
)
