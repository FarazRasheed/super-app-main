package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class SubAccountOnBoardingStatus {
    IN_PROGRESS, FEE_PENDING, CUSTOMER_PENDING, ADMIN_PENDING, CARD_PENDING, COMPLETE, FAILED
}