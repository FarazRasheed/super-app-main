package co.yap.networking.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class VerifyForgotPasscodeOtpRequest(
    @SerializedName("destination")
    val destination: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("emailOTP")
    val emailOTP: Boolean
)