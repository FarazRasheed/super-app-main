package co.yap.networking.transactions.responsedtos.purposepayment

import com.google.gson.annotations.SerializedName

data class PurposeOfPayment(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("archived")
    val archived: Boolean? = null,
    @SerializedName("bankTransfer")
    val bankTransfer: Boolean? = null,
    @SerializedName("cbwsi")
    var cbwsi: Boolean? = null,
    @SerializedName("cbwsiFee")
    var cbwsiFee: Boolean? = null,
    @SerializedName("domesticTransferAed")
    val domesticTransferAed: Boolean? = null,
    @SerializedName("domesticTransferNonAed")
    val domesticTransferNonAed: Boolean? = null,
    @SerializedName("nonChargeable")
    var nonChargeable: Boolean? = null,
    @SerializedName("purposeCategory")
    val purposeCategory: String? = null,
    @SerializedName("purposeCode")
    val purposeCode: String? = null,
    @SerializedName("purposeDescription")
    val purposeDescription: String? = null,
    @SerializedName("rmtBankAccount")
    val rmtBankAccount: Boolean? = null,
    @SerializedName("rmtCashPickup")
    val rmtCashPickup: Boolean? = null
)