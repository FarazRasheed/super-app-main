package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName

data class SMCoolingPeriodRequest(
    @SerializedName("beneficiaryId")
    val beneficiaryId: String,
    @SerializedName("productCode")
    val productCode: String
)