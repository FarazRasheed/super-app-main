package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

class SendMoneyTransferRequest(
    @SerializedName("beneficiaryId") var beneficiaryId: Int? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("purposeCode") var purposeCode: String? = null,
    @SerializedName("purposeReason") var purposeReason: String? = null,
    @SerializedName("fxRate") var fxRate: String? = null,
    @SerializedName("cbwsi") var cbwsi: Boolean? = null,
    @SerializedName("cbwsiFee") var cbwsiFee: Boolean? = null,
    @SerializedName("nonChargeable") var nonChargeable: Boolean? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("settlementAmount") var settlementAmount: Double? = null,
    @SerializedName("vat") var vat: String? = null,
    @SerializedName("totalAmount") var totalAmount: String? = null,
    @SerializedName("totalCharges") var totalCharges: String?=null,
    @SerializedName("remarks") var remarks: String?=null,
    @SerializedName("feeAmount") var feeAmount: String?=null
)