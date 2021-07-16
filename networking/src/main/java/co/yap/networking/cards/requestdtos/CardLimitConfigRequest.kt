package co.yap.networking.cards.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardLimitConfigRequest(
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String
) : Parcelable
