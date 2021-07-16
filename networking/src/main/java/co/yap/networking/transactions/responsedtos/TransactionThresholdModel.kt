package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

data class TransactionThresholdModel(
    @SerializedName("totalDebitAmount")
    var totalDebitAmount: Double?,
    @SerializedName("dailyLimit")
    var dailyLimit: Double?,
    @SerializedName("otpLimit")
    var otpLimit: Double?,
    @SerializedName("otpLimitY2Y")
    var otpLimitY2Y: Double?,
    @SerializedName("totalDebitAmountRemittance")
    var totalDebitAmountRemittance: Double?,
    @SerializedName("totalDebitAmountY2Y")
    var totalDebitAmountY2Y: Double?,
    @SerializedName("cbwsiPaymentLimit")
    var cbwsiPaymentLimit: Double?,
    @SerializedName("holdUAEFTSAmount")
    var holdUAEFTSAmount: Double?,
    @SerializedName("holdSwiftAmount")
    var holdSwiftAmount: Double?,
    @SerializedName("totalDebitAmountTopUpSupplementary")
    var totalDebitAmountTopUpSupplementary: Double?,
    @SerializedName("dailyLimitTopUpSupplementary")
    var dailyLimitTopUpSupplementary: Double?,
    @SerializedName("otpLimitTopUpSupplementary")
    var otpLimitTopUpSupplementary: Double?,
    @SerializedName("virtualCardBalanceLimit")
    var virtualCardBalanceLimit: Double?,
    @SerializedName("holdAmountIsIncludedInTotalDebitAmount")
    var holdAmountIsIncludedInTotalDebitAmount: Boolean?
)
