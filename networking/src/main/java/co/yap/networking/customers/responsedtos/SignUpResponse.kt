package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class SignUpResponse(@SerializedName("data") var data: String?=null) : ApiResponse()