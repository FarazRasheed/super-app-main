package co.yap.networking.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class VerifyOtpGenericRequest(@SerializedName("action") val action: String, @SerializedName("otp") val otp: String)