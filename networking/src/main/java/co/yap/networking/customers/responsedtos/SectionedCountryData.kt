package co.yap.networking.customers.responsedtos

import com.google.gson.annotations.SerializedName

data class SectionedCountryData(
    @SerializedName("active") var active: Boolean,
    @SerializedName("id") var id: Int,
    @SerializedName("isoCountryCode2Digit") var isoCountryCode2Digit: String,
    @SerializedName("isoCountryCode3Digit") var isoCountryCode3Digit: String,
    @SerializedName("name") var name: String,
    @SerializedName("signUpAllowed") var signUpAllowed: Boolean
)