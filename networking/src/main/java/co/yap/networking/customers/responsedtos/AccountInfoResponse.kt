package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class AccountInfoResponse(@SerializedName("data") val data: List<AccountInfo>) : ApiResponse()