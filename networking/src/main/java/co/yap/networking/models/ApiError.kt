package co.yap.networking.models

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("code") var statusCode: Int,
    @SerializedName("message") var message: String = "Sorry, that doesn't look right. We’re working on fixing it now. Please try again in sometime.",
    @SerializedName("actualCode") var actualCode: String = "-1"
)