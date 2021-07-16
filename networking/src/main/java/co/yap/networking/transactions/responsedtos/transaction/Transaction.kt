package co.yap.networking.transactions.responsedtos.transaction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    @SerializedName("accountUuid1")
    val accountUuid1: String? = null,
    @SerializedName("accountUuid2")
    val accountUuid2: String? = null,
    @SerializedName("adjustmentDate")
    val adjustmentDate: String? = null,
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("balanceAfter")
    val balanceAfter: Double? = null,
    @SerializedName("balanceBefore")
    val balanceBefore: Double? = null,
    @SerializedName("bankRefNumber")
    val bankRefNumber: String? = null,
    @SerializedName("beneficiaryId")
    val beneficiaryId: String? = null,
    @SerializedName("card1")
    val card1: String? = null,
    @SerializedName("card2")
    val card2: String? = null,
    @SerializedName("cardAcceptorLocation")
    val cardAcceptorLocation: String? = null,
    @SerializedName("cardHolderBillingAmount")
    val cardHolderBillingAmount: Double? = null,
    @SerializedName("cardHolderBillingCurrency")
    val cardHolderBillingCurrency: String? = null,
    @SerializedName("cardType")
    val cardType: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("customerId1")
    val customerId1: String? = null,
    @SerializedName("customerId2")
    val customerId2: String? = null,
    @SerializedName("debitIdentifier")
    val debitIdentifier: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("fxRate")
    val fxRate: String? = null,
    @SerializedName("iban1")
    val iban1: String? = null,
    @SerializedName("iban2")
    val iban2: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("initiator")
    val initiator: String? = null,
    @SerializedName("markupFees")
    val markupFees: Double? = null,
    @SerializedName("maskedCardNo")
    val maskedCardNo: String? = null,
    @SerializedName("merchantCategory")
    val merchantCategory: String? = null,
    @SerializedName("merchantCategoryName")
    var merchantCategoryName: String? = null,
    @SerializedName("merchantCode")
    val merchantCode: String? = null,
    @SerializedName("merchantLogo")
    val merchantLogo: String? = null,
    @SerializedName("merchantName")
    val merchantName: String? = null,
    @SerializedName("otherBankBranchName")
    val otherBankBranchName: String? = null,
    @SerializedName("otherBankCountry")
    val otherBankCountry: String? = null,
    @SerializedName("otherBankCurrency")
    val otherBankCurrency: String? = null,
    @SerializedName("otherBankName")
    val otherBankName: String? = null,
    @SerializedName("otherBranchAddress2")
    val otherBranchAddress2: String? = null,
    @SerializedName("paymentMode")
    val paymentMode: String? = null,
    @SerializedName("postedFees")
    val postedFees: Double? = null,
    @SerializedName("processorErrorCode")
    val processorErrorCode: String? = null,
    @SerializedName("processorErrorDescription")
    val processorErrorDescription: String? = null,
    @SerializedName("processorRefNumber")
    val processorRefNumber: String? = null,
    @SerializedName("productCode")
    var productCode: String? = null,
    @SerializedName("productName")
    val productName: String? = null,
    @SerializedName("purposeCode")
    val purposeCode: String? = null,
    @SerializedName("purposeReason")
    val purposeReason: String? = null,
    @SerializedName("receiverEmail")
    val receiverEmail: String? = null,
    @SerializedName("receiverMobileNo")
    val receiverMobileNo: String? = null,
    @SerializedName("receiverName")
    var receiverName: String? = null,
    @SerializedName("remarks")
    val remarks: String? = null,
    @SerializedName("senderEmail")
    val senderEmail: String? = null,
    @SerializedName("senderMobileNo")
    val senderMobileNo: String? = null,
    @SerializedName("senderName")
    var senderName: String? = null,
    @SerializedName("settlementAmount")
    val settlementAmount: Double? = null,
    @SerializedName("settlementCurrency")
    val settlementCurrency: String? = null,
    @SerializedName("settlementRate")
    val settlementRate: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("terminalId")
    val terminalId: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("totalAmount")
    val totalAmount: Double? = null,
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("transactionNote")
    var transactionNote: String? = null,
    @SerializedName("transactionNoteDate")
    var transactionNoteDate: String? = null,
    @SerializedName("txnRefNo")
    val txnRefNo: String? = null,
    @SerializedName("txnState")
    val txnState: String? = null,
    @SerializedName("txnType")
    val txnType: String? = null,
    @SerializedName("updatedBy")
    val updatedBy: String? = null,
    @SerializedName("updatedDate")
    var updatedDate: String? = null,
    @SerializedName("userType1")
    val userType1: String? = null,
    @SerializedName("userType2")
    val userType2: String? = null,
    @SerializedName("vatAmount")
    val vatAmount: Double? = null,
    @SerializedName("senderProfilePictureUrl")
    val senderProfilePictureUrl: String? = null,
    @SerializedName("receiverProfilePictureUrl")
    val receiverProfilePictureUrl: String? = null,
    @SerializedName("cancelReason")
    val cancelReason: String? = null

) : Parcelable