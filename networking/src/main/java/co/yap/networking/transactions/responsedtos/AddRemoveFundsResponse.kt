package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class AddRemoveFundsResponse(
    @SerializedName("data")
    val data: AddRemoveFunds? = null
) : ApiResponse()