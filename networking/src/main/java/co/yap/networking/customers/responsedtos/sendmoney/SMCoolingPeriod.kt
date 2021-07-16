package co.yap.networking.customers.responsedtos.sendmoney

import com.google.gson.annotations.SerializedName

data class SMCoolingPeriod(
    @SerializedName("coolingPeriodDuration")
    var coolingPeriodDuration: String? = null,
    @SerializedName("maxAllowedCoolingPeriodAmount")
    var maxAllowedCoolingPeriodAmount: String? = null,
    @SerializedName("createdOn")
    var createdOn: String? = null,
    @SerializedName("difference")
    var difference: Long? = null,
    @SerializedName("consumedAmount")
    var consumedAmount: Double? = null,
    @SerializedName("beneficiaryId")
    var beneficiaryId: String? = null,
    @SerializedName("productCode")
    var productCode: String? = null
)