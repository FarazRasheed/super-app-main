package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class HouseholdOnBoardingResponse(

    @SerializedName("data")
    val data: Data? = null
) : ApiResponse() {
    data class Data(
        @SerializedName("passcode")
        val passcode: String? = null,
        @SerializedName("parentUUID")
        val parentUUID: String? = null
    ) : ApiResponse()
}
