package co.yap.networking.customers.responsedtos.documents

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetMoreDocumentsResponse(
    @SerializedName("data")
    var data: Data?,
    @SerializedName("errors")
    var errors: Any?
) : ApiResponse() {
    data class Data(
        @SerializedName("active")
        var active: Boolean? = false,
        @SerializedName("customerDocuments")
        var customerDocuments: List<CustomerDocument>? = ArrayList(),
        @SerializedName("customerUUID")
        var customerUUID: String? = "",
        @SerializedName("dateExpiry")
        var dateExpiry: String? = null,
        @SerializedName("dateIssue")
        var dateIssue: String? = "",
        @SerializedName("dob")
        var dob: String? = "",
        @SerializedName("documentType")
        var documentType: String? = "",
        @SerializedName("firstName")
        var firstName: String? = "",
        @SerializedName("fullName")
        var fullName: String? = "",
        @SerializedName("gender")
        var gender: String? = "",
        @SerializedName("lastName")
        var lastName: String? = "",
        @SerializedName("legalStatus")
        var legalStatus: Any? = null,
        @SerializedName("licenseNumber")
        var licenseNumber: Any? = null,
        @SerializedName("nationality")
        var nationality: String? = null,
        @SerializedName("passportNumber")
        var passportNumber: Any? = null,
        @SerializedName("registrationAuthority")
        var registrationAuthority: Any? = null,
        @SerializedName("registrationNumber")
        var registrationNumber: Any? = null,
        @SerializedName("tradeName")
        var tradeName: Any? = null
    ) {
        data class CustomerDocument(
            @SerializedName("active")
            var active: Boolean? = false,
            @SerializedName("contentType")
            var contentType: String? = "",
            @SerializedName("createdBy")
            var createdBy: String? = "",
            @SerializedName("creationDate")
            var creationDate: String? = "",
            @SerializedName("customerId")
            var customerId: String? = "",
            @SerializedName("customerUUID")
            var customerUUID: String? = "",
            @SerializedName("documentInformation")
            var documentInformation: DocumentInformation? = DocumentInformation(),
            @SerializedName("documentType")
            var documentType: String? = "",
            @SerializedName("expired")
            var expired: Boolean? = false,
            @SerializedName("fileName")
            var fileName: String? = "",
            @SerializedName("pageNo")
            var pageNo: Int? = 0,
            @SerializedName("updatedBy")
            var updatedBy: String? = "",
            @SerializedName("updatedDate")
            var updatedDate: String? = "",
            @SerializedName("uploadDate")
            var uploadDate: String? = ""
        ) {
            @Parcelize
            data class DocumentInformation(
                @SerializedName("active")
                var active: Boolean? = false,
                @SerializedName("createdBy")
                var createdBy: String? = "",
                @SerializedName("creationDate")
                var creationDate: String? = "",
                @SerializedName("customerUUID")
                var customerUUID: String? = "",
                @SerializedName("dateExpiry")
                var dateExpiry: String? = "",
                @SerializedName("dateIssue")
                var dateIssue: String? = "",
                @SerializedName("dob")
                var dob: String? = "",
                @SerializedName("documentType")
                var documentType: String? = "",
                @SerializedName("firstName")
                var firstName: String? = "",
                @SerializedName("fullName")
                var fullName: String? = "",
                @SerializedName("gender")
                var gender: String? = "",
                @SerializedName("identityNo")
                var identityNo: String? = "",
                @SerializedName("lastName")
                var lastName: String? = "",
                @SerializedName("nationality")
                var nationality: String? = "",
                @SerializedName("updatedBy")
                var updatedBy: String? = "",
                @SerializedName("updatedDate")
                var updatedDate: String? = ""
            ) : Parcelable
        }
    }
}