package co.yap.networking.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDetails(
    @SerializedName("title")
    val title: String?,
    @SerializedName("fromCard")
    val fromCard: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("txnType")
    val txnType: String?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("totalAmount")
    val totalAmount: Double?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("fromBalanceBefore")
    val fromBalanceBefore: Double?,
    @SerializedName("fromBalanceAfter")
    val fromBalanceAfter: Double?,
    @SerializedName("productCode")
    val productCode: String?,
    @SerializedName("fromAccountUUID")
    val fromAccountUUID: String?,
    @SerializedName("initiator")
    val initiator: String?,
    @SerializedName("transactionId")
    val transactionId: String?,
    @SerializedName("fromCustomerId")
    val fromCustomerId: String?,
    @SerializedName("otpVerificationReq")
    val otpVerificationReq: Boolean?,
    @SerializedName("creationDate")
    val creationDate: String?,
    @SerializedName("paymentMode")
    val paymentMode: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("feeAmount")
    val feeAmount: Double?,
    @SerializedName("vat")
    val vat: Double?,
    @SerializedName("transactionNote")
    val transactionNote: String?,
    @SerializedName("senderName")
    val senderName: String?,
    @SerializedName("receiverName")
    val receiverName: String?,
    @SerializedName("postedFees")
    val postedFees: String?
):Parcelable

