package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class ValidateDeviceResponse(@SerializedName("data") var data: Boolean? = false) : ApiResponse()