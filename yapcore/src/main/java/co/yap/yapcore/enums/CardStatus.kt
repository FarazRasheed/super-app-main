package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class CardStatus {
    ACTIVE, BLOCKED, INACTIVE, HOTLISTED, EXPIRED, PIN_BLOCKED;

    //Zain ul Abe Din Sohail Zahid There is new status added in card status PIN_BLOCKED.
    // Please handle this at your side also because currently at backend we have added
    // this status and now cards are not appearing on mobile app.
}