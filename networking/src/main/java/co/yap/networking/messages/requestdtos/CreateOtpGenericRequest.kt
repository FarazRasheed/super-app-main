package co.yap.networking.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class CreateOtpGenericRequest(@SerializedName("action") val action: String)