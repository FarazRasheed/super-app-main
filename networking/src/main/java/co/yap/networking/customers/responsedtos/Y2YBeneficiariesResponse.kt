package co.yap.networking.customers.responsedtos

import co.yap.networking.customers.requestdtos.Contact
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName


data class Y2YBeneficiariesResponse(
    @SerializedName("data")
    val data: List<Contact>? = null,
    @SerializedName("errors")
    var errors: Any? = null
) : ApiResponse()