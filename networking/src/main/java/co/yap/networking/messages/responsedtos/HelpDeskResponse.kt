package co.yap.networking.messages.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName


data class HelpDeskResponse(
    @SerializedName("data")
    var data: String? = ""
) : ApiResponse()
