package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("amount") val amount: Int?,
    @SerializedName("category") val category: String?,
    @SerializedName("count") val count: Int?,
    @SerializedName("creationDate") val creationDate: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("fromAccountUUID") val fromAccountUUID: String?,
    @SerializedName("fromBalanceAfter") val fromBalanceAfter: Double?,
    @SerializedName("fromBalanceBefore") val fromBalanceBefore: Int?,
    @SerializedName("fromCard") val fromCard: String?,
    @SerializedName("fromCustomerId") val fromCustomerId: String?,
    @SerializedName("fromIBAN") val fromIBAN: String?,
    @SerializedName("fromUserType") val fromUserType: String?,
    @SerializedName("initiator") val initiator: String?,
    @SerializedName("otpVerificationReq") val otpVerificationReq: Boolean?,
    @SerializedName("paymentMode") val paymentMode: String?,
    @SerializedName("processorRefNumber") val processorRefNumber: String?,
    @SerializedName("productCode") val productCode: String?,
    @SerializedName("productName") val productName: String?,
    @SerializedName("remarks") val remarks: String?,
    @SerializedName("senderName") val senderName: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("totalAmount") val totalAmount: Int?,
    @SerializedName("transactionId") val transactionId: String?,
    @SerializedName("txnCode") val txnCode: String?,
    @SerializedName("txnType") val txnType: String?
)