package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class AddHouseholdEmailRequest(
    @SerializedName("email")
    val email: String
)