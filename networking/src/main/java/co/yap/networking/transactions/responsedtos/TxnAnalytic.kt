package co.yap.networking.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TxnAnalytic(
    @SerializedName("logoUrl")
    val logoUrl: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("totalSpending")
    val totalSpending: String? = null,
    @SerializedName("totalSpendingInPercentage")
    val totalSpendingInPercentage: Double? = null,
    @SerializedName("txnCount")
    val txnCount: Int? = null,
    @SerializedName("categories")
    val categories: ArrayList<String>? = null
): Parcelable
