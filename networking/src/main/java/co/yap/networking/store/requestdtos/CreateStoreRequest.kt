package co.yap.networking.store.requestdtos

import com.google.gson.annotations.SerializedName

data class CreateStoreRequest(
    @SerializedName("id")
    var id: String = ""
)
