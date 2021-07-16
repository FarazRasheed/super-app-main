package co.yap.networking.customers.responsedtos.beneficiary

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopUpCard(
    @SerializedName("id") val id: String? = "",
    @SerializedName("logo") val logo: String? = "",
    @SerializedName("expiry") val expiry: String? = "",
    @SerializedName("number") var number: String? = "",
    @SerializedName("alias") val alias: String? = "",
    @SerializedName("color") val color: String? = ""
) : ApiResponse(), Parcelable