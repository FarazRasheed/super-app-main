package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class BirthInfoRequest(
    @SerializedName("countryOfBirth")
    val countryOfBirth: String,
    @SerializedName("cityOfBirth")
    val cityOfBirth: String
)