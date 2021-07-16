package co.yap.networking.cards.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CardBalanceResponseDTO(
    @SerializedName("data")
    val data: CardBalance? = null
) : ApiResponse()
