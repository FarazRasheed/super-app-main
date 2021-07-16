package co.yap.networking.customers.models

import com.google.gson.annotations.SerializedName

class Document {
    @SerializedName ("contentType")var contentType: String? = ""
    @SerializedName("imageText") var imageText: String? = ""
    @SerializedName("pages") var pages: List<DocumentPage>? = arrayListOf()
}