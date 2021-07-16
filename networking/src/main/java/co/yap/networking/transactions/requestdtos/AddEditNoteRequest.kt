package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class AddEditNoteRequest(
    @SerializedName("transactionId") val transactionId: String?,
    @SerializedName("transactionNote") val transactionNote: String?
)