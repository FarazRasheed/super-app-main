package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class DemographicDataRequest(
    @SerializedName("action") val action: String? = null,
    @SerializedName("osVersion") val osVersion: String? = null,
    @SerializedName("deviceId") val deviceId: String? = null,
    @SerializedName("deviceName") val deviceName: String? = null,
    @SerializedName("deviceModel") val deviceModel: String? = null,
    @SerializedName("osType") val osType: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("clientId") val clientId: String? = null,
    @SerializedName("clientSecret") val clientSecret: String? = null,
    @SerializedName("otp") val otp: String? = null
)