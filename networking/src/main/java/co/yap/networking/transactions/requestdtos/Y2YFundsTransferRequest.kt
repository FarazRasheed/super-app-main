package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class Y2YFundsTransferRequest(
    @SerializedName("receiverUUID") var receiverUUID: String?,
    @SerializedName("beneficiaryName") var beneficiaryName: String?,
    @SerializedName("amount") var amount: String?,
    @SerializedName("otpVerificationReq") var otpVerificationReq: Boolean?,
    @SerializedName("transactionNote") var transactionNote: String?,
    @SerializedName("remarks") var remarks: String?
) {
}