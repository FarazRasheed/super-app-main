package co.yap.networking.cards.requestdtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChangeCardPinRequest(
    @SerializedName("oldPin")
    val oldPin: String,
    @SerializedName("newPin")
    val newPin: String,
    @SerializedName("confirmPin")
    val confirmPin: String,
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String
) : Serializable