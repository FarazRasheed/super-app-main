package co.yap.networking.cards.requestdtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardsHotlistRequest(
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String,
    @SerializedName("hotListReason")
    val hotListReason: String
) : Serializable