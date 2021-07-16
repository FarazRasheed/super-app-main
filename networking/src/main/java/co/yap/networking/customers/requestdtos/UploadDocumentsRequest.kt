package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName
import java.util.*

data class UploadDocumentsRequest(
    @SerializedName("filePaths") val filePaths: List<String>,
    @SerializedName("documentType") val documentType: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("middleName") val middleName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("nationality") val nationality: String,
    @SerializedName("dateExpiry") val dateExpiry: Date,
    @SerializedName("dateIssue") val dateIssue: Date? = null,
    @SerializedName("dob") val dob: Date,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("gender") val gender: String, // M/F
    @SerializedName("identityNo") val identityNo: String? = null,
    @SerializedName("countryIsSanctioned") val countryIsSanctioned: Boolean? = null
)