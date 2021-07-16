package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class VerifyPasscodeRequest(
    @SerializedName("passcode")
    val passcode: String
)