package co.yap.networking.customers.responsedtos.beneficiary

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class BankParamsResponse(
    @SerializedName("data")
    val data: Data?
) : ApiResponse()

data class Data(
    @SerializedName("other_bank_country")
    val otherBankCountry: String?,
    @SerializedName("params")
    val params: List<BankParams>
) : ApiResponse()

data class BankParams(
    @SerializedName("id")
    val id: String?,
    @SerializedName("other_bank_country")
    val otherBankCountry: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("is_mandatory")
    val isMandatory: String?,
    @SerializedName("min_characters")
    val minCharacters: String?,
    @SerializedName("max_characters")
    val maxCharacters: String?,
    @Transient
    var data: String? = ""

) : ApiResponse()