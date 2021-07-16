package co.yap.networking.customers.responsedtos.sendmoney

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class SMCoolingPeriodResponseDTO : ApiResponse() {
    @SerializedName("data")
    var data: SMCoolingPeriod? = null
}