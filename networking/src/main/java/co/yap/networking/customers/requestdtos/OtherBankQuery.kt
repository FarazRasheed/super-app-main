package co.yap.networking.customers.requestdtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtherBankQuery(
    @SerializedName("other_bank_country")
    var other_bank_country: String? = "",
    @SerializedName("max_records")
    var max_records: Int? = 0,
    @SerializedName("params")
    val params: ArrayList<Params>? = ArrayList()
) : ApiResponse(), Parcelable {
    @Parcelize
    data class Params(
        @SerializedName("id") var id: String? = null,
        @SerializedName("value") var value: String? = null
    ) : ApiResponse(), Parcelable
}