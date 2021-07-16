package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class AddEditNoteResponse(
    @SerializedName("data")
    val data: String? = ""
) : ApiResponse()