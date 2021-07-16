package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class CardDeliveryStatus {
    BOOKED, SHIPPING, SHIPPED, FAILED, ORDERED;
}