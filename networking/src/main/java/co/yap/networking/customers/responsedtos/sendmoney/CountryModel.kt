package co.yap.networking.customers.responsedtos.sendmoney

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class CountryModel(
    @SerializedName("data")
    var data: List<Country>? = arrayListOf(),
    @SerializedName("errors")
    var errors: Any?
) : ApiResponse()

@Parcelize
data class Country(
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("isoCountryCode2Digit")
    var isoCountryCode2Digit: String? = "",
    @SerializedName("isoCountryCode3Digit")
    var isoCountryCode3Digit: String? = "",
    @SerializedName("isoNum")
    var isoNum: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("signUpAllowed")
    var signUpAllowed: Boolean? = false,
    @SerializedName("ibanMandatory")
    var ibanMandatory: Boolean? = false,
    @SerializedName("edit")
    var edit: Boolean? = false,
    @SerializedName("currencyList")
    var currencyList: List<Currency>? = arrayListOf()
) : Parcelable, ApiResponse()

data class CountryDataWithISODigit(@SerializedName("data") var data: Country) : ApiResponse()

@Parcelize
data class Currency(
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("default")
    var default: Boolean? = false,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("cashPickUp")
    var cashPickUp: Boolean? = false,
    @SerializedName("rmtCountry")
    var rmtCountry: Boolean? = false,
    @SerializedName("creationDate")
    var creationDate: String? = "",
    @SerializedName("createdBy")
    var createdBy: String? = "",
    @SerializedName("updatedDate")
    var updatedDate: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("symbol")
    var symbol: Boolean? = false,
    @SerializedName("isoNum")
    var isoNum: String? = ""
) : Parcelable
