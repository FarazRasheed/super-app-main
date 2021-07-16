package co.yap.networking.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardFee(
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("amount")
    val amount: String?
) : Parcelable