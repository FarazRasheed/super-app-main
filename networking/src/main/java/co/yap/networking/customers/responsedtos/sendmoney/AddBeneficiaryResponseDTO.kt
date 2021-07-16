package co.yap.networking.customers.responsedtos.sendmoney

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName


class AddBeneficiaryResponseDTO : ApiResponse() {

    @SerializedName("data")
    var data: Beneficiary? = null
}
