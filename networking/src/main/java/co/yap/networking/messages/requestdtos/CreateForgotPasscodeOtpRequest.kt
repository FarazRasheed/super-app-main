package co.yap.networking.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class CreateForgotPasscodeOtpRequest(
    @SerializedName("destination")
    val destination: String,
    @SerializedName("emailOTP")
    val emailOTP: Boolean
)