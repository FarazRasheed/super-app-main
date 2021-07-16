package co.yap.networking.transactions.responsedtos


import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class InternationalFundsTransferReasonList(
    @SerializedName("errors")
    val errors: String? = null, // null
    @SerializedName("data")
    val data: ArrayList<ReasonList>? = arrayListOf()
) : Parcelable, ApiResponse() {
    @Parcelize
    data class ReasonList(
        @SerializedName("reason")
        val reason: String?=null, // REAL ESTATE RELATED PAYMENTS
        @SerializedName("code")
        val code: String?=null // 26
    ) : Parcelable
}