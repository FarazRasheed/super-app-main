package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class SendMoneyBeneficiaryType(val type: String) {
    SWIFT("SWIFT"),
    RMT("RMT"),
    CASHPAYOUT("CASHPAYOUT"),
    DOMESTIC("DOMESTIC"),
    INTERNAL_TRANSFER("INTERNAL_TRANSFER"),
    UAEFTS("UAEFTS"),
    DOMESTIC_TRANSFER("DOMESTIC_TRANSFER")
}