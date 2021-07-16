package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class AnalyticsMerchantType(merchant: String) {
    Amazon("Amazon"),
    Uber("Uber"),
    Emirates("Emirates"),
    CANDY("CANDY"),
    Others("Others")
}