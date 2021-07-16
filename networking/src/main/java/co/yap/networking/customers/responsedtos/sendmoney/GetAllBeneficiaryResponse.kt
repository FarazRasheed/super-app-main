package co.yap.networking.customers.responsedtos.sendmoney

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class GetAllBeneficiaryResponse(
    @SerializedName("data")
    var data: List<Beneficiary>,
    @SerializedName("errors")
    var errors: Any?
) : ApiResponse()

