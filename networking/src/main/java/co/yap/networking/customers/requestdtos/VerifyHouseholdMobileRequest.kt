package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class VerifyHouseholdMobileRequest(
    @SerializedName("countryCode")
    var countryCode: String,
    @SerializedName("mobileNo")
    var mobileNo: String?
)