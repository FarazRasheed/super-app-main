package co.yap.networking.customers.requestdtos


import com.google.gson.annotations.SerializedName

data class SaveReferalRequest(
    @SerializedName("inviterCustomerId")
    var inviterCustomerId: String,
    @SerializedName("referralDate")
    var referralDate: String
)