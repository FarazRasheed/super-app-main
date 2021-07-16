package co.yap.networking.cards.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class AddSpareVirualCardResponse(
    @SerializedName("data")
    val data: Card?
):ApiResponse()