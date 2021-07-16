package co.yap.networking.transactions.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Order(
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("amount")
    var amount: String? = ""
) : Parcelable