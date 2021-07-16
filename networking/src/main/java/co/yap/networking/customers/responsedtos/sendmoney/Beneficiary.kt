package co.yap.networking.customers.responsedtos.sendmoney

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beneficiary(

    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("beneficiaryId")
    var beneficiaryId: String? = null,
    @SerializedName("accountUuid")
    var accountUuid: String? = null,
    @SerializedName("beneficiaryType")
    var beneficiaryType: String? = null,
    @SerializedName("mobileNo")
    var mobileNo: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("beneficiaryUuid")
    var beneficiaryUuid: String? = null,
    @SerializedName("accountNo")
    var accountNo: String? = null,
    @SerializedName("lastUsedDate")
    var lastUsedDate: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("firstName")
    var firstName: String? = null,
    @SerializedName("lastName")
    var lastName: String? = null,
    @SerializedName("swiftCode")
    var swiftCode: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("bankName")
    var bankName: String? = null,
    @SerializedName("branchName")
    var branchName: String? = null,
    @SerializedName("bankCity")
    var bankCity: String? = null,
    @SerializedName("branchAddress")
    var branchAddress: String? = null,
    @SerializedName("identifierCode1")
    var identifierCode1: String? = null,
    @SerializedName("identifierCode2")
    var identifierCode2: String? = null,
    @SerializedName("cbwsicompliant")
    var cbwsicompliant: Boolean? = null,
    // assuming this field for profile picture but not sure whether to add pic or just go with initials only
    @SerializedName("beneficiaryPictureUrl")
    var beneficiaryPictureUrl: String? = null,
    @SerializedName("beneficiaryCountry")
    var beneficiaryCountry: String? = null,
    @SerializedName("countryOfResidence")
    var countryOfResidence: String? = null,
    @SerializedName("countryOfResidenceName")
    var countryOfResidenceName: String? = null,
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = null
) : Parcelable {
    fun fullName() = "$firstName $lastName"
}
