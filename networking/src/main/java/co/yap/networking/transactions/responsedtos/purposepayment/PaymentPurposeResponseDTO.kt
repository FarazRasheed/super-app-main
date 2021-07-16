package co.yap.networking.transactions.responsedtos.purposepayment

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class PaymentPurposeResponseDTO(
    @SerializedName("data")
    val data: List<PurposeOfPayment>? = null
) : ApiResponse()
