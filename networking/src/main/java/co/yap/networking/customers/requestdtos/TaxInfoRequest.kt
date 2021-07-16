package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class TaxInfoRequest(
    @SerializedName("usNationalForTax")
    val usNationalForTax: Boolean,
    @SerializedName("submit")
    val submit: Boolean,
    @SerializedName("taxInformationDetails")
    val taxInfoDetails: ArrayList<TaxInfoDetailRequest>
)

data class TaxInfoDetailRequest(
    @SerializedName("country")
    val country: String,
    @SerializedName("tinAvailable")
    val tinAvailable: Boolean,
    @SerializedName("reasonInCaseNoTin")
    val reasonInCaseNoTin: String,
    @SerializedName("tinNumber")
    val tinNumber: String
)