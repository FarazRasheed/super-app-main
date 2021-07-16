package co.yap.networking.customers.models

import com.google.gson.annotations.SerializedName

class DocumentPage {
    @SerializedName("pageNo") var pageNo: Int? = -1
    @SerializedName("imageURL") var imageURL: String? = ""
}