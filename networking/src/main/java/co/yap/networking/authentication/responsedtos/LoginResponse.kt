package co.yap.networking.authentication.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("id_token")
    val id_token: String? = null
) : ApiResponse()