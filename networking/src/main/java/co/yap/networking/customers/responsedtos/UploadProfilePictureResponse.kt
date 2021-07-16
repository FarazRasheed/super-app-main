package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class UploadProfilePictureResponse(
    @SerializedName("data")var data: Data? = Data(),
    @SerializedName("errors") var errors: Any?
) : ApiResponse() {
    data class Data(
        @SerializedName("imageURL") var imageURL: String?=""
    )
}