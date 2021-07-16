package co.yap.yapcore.enums

enum class OTPActions {
    //top up
    TOP_UP_SUPPLEMENTARY,

    // change personal detail
    CHANGE_EMAIL,
    CHANGE_MOBILE_NO,
    FORGOT_CARD_PIN,
    FORGOT_PASS_CODE,

    // add beneficiary otp actions
    SWIFT_BENEFICIARY,
    RMT_BENEFICIARY,
    CASHPAYOUT_BENEFICIARY,
    DOMESTIC_BENEFICIARY,
    IS_NEW_BENEFICIARY,

    // transfer fund otp actions
    SWIFT,
    UAEFTS,
    RMT,
    CASHPAYOUT,
    DOMESTIC_TRANSFER,
    INTERNAL_TRANSFER,
    Y2Y
}