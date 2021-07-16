package co.yap.networking.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardStatement(
    @SerializedName("statementURL")
    val statementURL: String? = "",
    @SerializedName("month")
    val month: String? = "",
    @SerializedName("year")
    val year: String? = ""
) : Parcelable
