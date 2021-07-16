package co.yap.networking.cards.requestdtos

import com.google.gson.annotations.SerializedName


data class CreateCardPinRequest(
    @SerializedName("newPin") val newPin: String
)
