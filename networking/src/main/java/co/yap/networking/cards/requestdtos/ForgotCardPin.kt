package co.yap.networking.cards.requestdtos

import com.google.gson.annotations.SerializedName

data class ForgotCardPin(@SerializedName("newPin") var newPin: String, @SerializedName("token") var token: String)