package co.yap.networking.customers.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class SectionedCountriesResponseDTO(
    @SerializedName("data") var data: List<SectionedCountryData>,
    @SerializedName("errors") var errors: Any?
) : ApiResponse()