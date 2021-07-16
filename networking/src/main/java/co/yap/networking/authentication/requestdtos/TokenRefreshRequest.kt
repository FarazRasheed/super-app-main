package co.yap.networking.authentication.requestdtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("id_token")
    val id_token: String? = null,
    @SerializedName("grant_type")
    val grant_type: String? = null
) : ApiResponse()