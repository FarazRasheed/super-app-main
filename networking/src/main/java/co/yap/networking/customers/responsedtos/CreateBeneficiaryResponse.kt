package co.yap.networking.customers.responsedtos

import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CreateBeneficiaryResponse(
    @SerializedName("data")
    val data: TopUpCard? = null
) : ApiResponse()