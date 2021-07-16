package co.yap.networking.customers.requestdtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("countryCode")
    var countryCode: String? = "",
    @SerializedName("mobileNo")
    var mobileNo: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("beneficiaryPictureUrl")
    var beneficiaryPictureUrl: String? = "",
    @SerializedName("yapUser")
    var yapUser: Boolean? = false,
    @SerializedName("accountDetailList")
    val accountDetailList: List<Data>? = null,
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = ""
) : ApiResponse(), Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("accountNo")
        val accountNo: String? = "",
        @SerializedName("accountType")
        val accountType: String? = "",
        @SerializedName("accountUuid")
        val accountUuid: String? = ""
    ) : ApiResponse(), Parcelable
}