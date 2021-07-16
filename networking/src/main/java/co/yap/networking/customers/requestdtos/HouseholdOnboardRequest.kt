package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HouseholdOnboardRequest(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("countryCode")
    val countryCode: String? = null,
    @SerializedName("mobileNo")
    val mobileNo: String? = null,
    @SerializedName("accountType")
    val accountType: String? = null,
    @SerializedName("feeFrequency")
    val feeFrequency: String
) : Serializable
