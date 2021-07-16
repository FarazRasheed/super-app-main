package co.yap.networking.transactions.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopUpTransactionRequest(
    @SerializedName("3DSecureId")
    val `3DSecureId`: String? = null,
    @SerializedName("beneficiaryId")
    val beneficiaryId: Int? = null,
    @SerializedName("order")
    val order: Order? = null,
    @SerializedName("securityCode")
    val securityCode: String? = null
) : Parcelable