package co.yap.networking.transactions.responsedtos.transaction

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemittanceFeeResponse(
    @SerializedName("errors")
    val errors: String? = null,
    @SerializedName("data")
    val data: RemittanceFee? = null
) : Parcelable, ApiResponse() {

    @Parcelize
    data class RemittanceFee(
        @SerializedName("feeType")
        val feeType: String? = null,
        @SerializedName("displayOnly")
        val displayOnly: Boolean? = false,
        @SerializedName("fixedAmount")
        val fixedAmount: Double? = 0.0,
        @SerializedName("feeCurrency")
        val feeCurrency: String? = null,
        @SerializedName("tierRateDTOList")
        val tierRateDTOList: List<TierRateDTO>? = arrayListOf()
    ) : Parcelable {
        @Parcelize
        data class TierRateDTO(
            @SerializedName("feeAmount")
            var feeAmount: Double? = 0.0, // 10.0
            @SerializedName("vatAmount")
            var vatAmount: Double? = 0.0, // 0.5
            @SerializedName("uuid")
            val uuid: String? = null,
            @SerializedName("amountFrom")
            val amountFrom: Double? = 0.0,
            @SerializedName("amountTo")
            val amountTo: Double? = 0.0,
            @SerializedName("createdBy")
            val createdBy: String? = null,
            @SerializedName("createdOn")
            val createdOn: String? = null,
            @SerializedName("updatedBy")
            val updatedBy: String? = null,
            @SerializedName("updatedOn")
            val updatedOn: String? = null,
            @SerializedName("feeUuid")
            val feeUuid: String? = null,
            @SerializedName("feePercentage")
            var feePercentage: String? = null,
            @SerializedName("vatPercentage")
            var vatPercentage: String? = null,
            @SerializedName("feeInPercentage")
            var feeInPercentage: Boolean? = null
        ) : Parcelable
    }
}