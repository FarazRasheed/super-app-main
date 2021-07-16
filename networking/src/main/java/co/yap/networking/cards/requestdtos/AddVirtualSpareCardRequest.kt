package co.yap.networking.cards.requestdtos

import com.google.gson.annotations.SerializedName

data class AddVirtualSpareCardRequest(
    @SerializedName("cardName") val cardName: String? = null
)