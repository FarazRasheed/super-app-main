package co.yap.networking.customers.responsedtos.beneficiary

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class TopUpBeneficiariesResponse(
    @SerializedName("data")
    val data: MutableList<TopUpCard>? = null
) : ApiResponse()
