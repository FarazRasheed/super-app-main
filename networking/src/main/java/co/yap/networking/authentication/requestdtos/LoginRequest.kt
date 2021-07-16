package co.yap.networking.authentication.requestdtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("grant_type")
    val grant_type: String? = null,
    @SerializedName("client_id")
    val client_id: String? = null,
    @SerializedName("client_secret")
    val client_secret: String? = null,
    @SerializedName("device_id")
    val device_id: String? = null
) : ApiResponse()