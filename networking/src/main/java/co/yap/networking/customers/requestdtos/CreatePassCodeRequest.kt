package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class CreatePassCodeRequest(
    @SerializedName("passcode")
    val passcode: String
)