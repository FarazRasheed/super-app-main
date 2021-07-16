package co.yap.yapcore.helpers.extentions

import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode

fun Beneficiary?.getProductCode(): String {
    this?.let { beneficiary ->
        return when (beneficiary.beneficiaryType) {
            SendMoneyBeneficiaryType.CASHPAYOUT.type -> TransactionProductCode.CASH_PAYOUT.pCode
            SendMoneyBeneficiaryType.UAEFTS.type -> TransactionProductCode.UAEFTS.pCode
            SendMoneyBeneficiaryType.DOMESTIC.type -> TransactionProductCode.DOMESTIC.pCode
            SendMoneyBeneficiaryType.RMT.type -> TransactionProductCode.RMT.pCode
            SendMoneyBeneficiaryType.SWIFT.type -> TransactionProductCode.SWIFT.pCode
            else -> ""
        }
    } ?: return ""
}

fun Beneficiary?.getBeneficiaryTransferType(): FeatureSet {
    this?.let { beneficiary ->
        return when (beneficiary.beneficiaryType) {
            SendMoneyBeneficiaryType.UAEFTS.type -> FeatureSet.UAEFTS_TRANSFER
            SendMoneyBeneficiaryType.DOMESTIC.type -> FeatureSet.DOMESTIC_TRANSFER
            SendMoneyBeneficiaryType.RMT.type -> FeatureSet.RMT_TRANSFER
            SendMoneyBeneficiaryType.SWIFT.type -> FeatureSet.SWIFT_TRANSFER
            else -> FeatureSet.NONE
        }
    } ?: return FeatureSet.NONE
}

fun Beneficiary?.isRMTAndSWIFT(): Boolean {
    this?.let { beneficiary ->
        return when (beneficiary.beneficiaryType) {
            SendMoneyBeneficiaryType.RMT.type, SendMoneyBeneficiaryType.SWIFT.type -> true
            else -> false
        }
    } ?: return false
}