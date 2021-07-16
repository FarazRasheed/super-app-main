package co.yap.yapcore.firebase

import androidx.annotation.Keep

@Keep
enum class FirebaseEvents(val event: String) {
    VERIFY_NUMBER("verifynumber"),
    SCAN_ID("scanid"),
    CONFIRM_ID("confirmid"),
    DELIVERY_STARTED("deliverystarted"),
    DELIVERY_CONFIRMED("deliveryconfirmed")
}