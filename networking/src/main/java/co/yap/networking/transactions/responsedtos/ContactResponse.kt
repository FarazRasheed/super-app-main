package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("errors")  val errors: Any?
)