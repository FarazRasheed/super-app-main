package co.yap.networking.customers.responsedtos

import co.yap.networking.customers.models.Document
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class GetDocumentsResponse : ApiResponse() {
    @SerializedName("data") var data: List<Document>? = arrayListOf()
    @SerializedName("documentType") var documentType: String = ""
}