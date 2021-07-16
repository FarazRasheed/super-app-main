package co.yap.networking.cards.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class GetCardsResponse(
    @SerializedName("data")
    val data: ArrayList<Card>? = null
) : ApiResponse()
