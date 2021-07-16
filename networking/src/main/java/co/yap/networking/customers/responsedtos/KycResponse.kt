package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class KycResponse(
    @SerializedName("data")
    val data: Data?
) : ApiResponse() {
    data class Data(
        @SerializedName("check_composite")
        val check_composite: String,
        @SerializedName("check_date_of_birth")
        val check_date_of_birth: String,
        @SerializedName("check_expiration_date")
        val check_expiration_date: String,
        @SerializedName("check_number")
        val check_number: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("date_of_birth")
        val date_of_birth: String,
        @SerializedName("expiration_date")
        val expiration_date: String,
        @SerializedName("method")
        val method: String,
        @SerializedName("mrz_type")
        val mrz_type: String,
        @SerializedName("names")
        val names: String,
        @SerializedName("nationality")
        val nationality: String,
        @SerializedName("number")
        val number: String,
        @SerializedName("optional1")
        val optional1: String,
        @SerializedName("optional2")
        val optional2: String,
        @SerializedName("sex")
        val sex: String?=null,
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("surname")
        val surname: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("valid_composite")
        val valid_composite: Boolean,
        @SerializedName("valid_date_of_birth")
        val valid_date_of_birth: Boolean,
        @SerializedName("valid_expiration_date")
        val valid_expiration_date: Boolean,
        @SerializedName("valid_number")
        val valid_number: Boolean,
        @SerializedName("valid_score")
        val valid_score: Int,
        @SerializedName("alpha_2_code")
        val isoCountryCode2Digit: String,
        @SerializedName("alpha_3_code")
        val isoCountryCode3Digit: String,
        @SerializedName("country_nation")
        val country_nation: String
    ) : ApiResponse()
}