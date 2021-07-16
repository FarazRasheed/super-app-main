package co.yap.modules.others.helper


object Constants {

    const val CARD_TYPE_DEBIT: String = "DEBIT"
    const val CARD_TYPE_PREPAID: String = "PREPAID"

    const val TEXT_PRIMARY_CARD: String = "Primary card"
    const val TEXT_SPARE_CARD_VIRTUAL: String = "Spare virtual card"
    const val TEXT_SPARE_CARD_PHYSICAL: String = "Spare physical card"

    const val EVENT_ADD_CARD_NAME: Int = 1
    const val EVENT_CHANGE_PIN: Int = 2
    const val EVENT_VIEW_STATEMENTS: Int = 3
    const val EVENT_REPORT_CARD: Int = 4
    const val EVENT_REMOVE_CARD: Int = 5
    const val EVENT_FORGOT_CARD_PIN: Int = 6
    const val EVENT_LOST_STOLEN_CARD: Int = 7

    const val REQUEST_CARD_NAME_UPDATED: Int = 55
    const val REQUEST_ADD_REMOVE_FUNDS: Int = 56
    const val REQUEST_REPORT_LOST_OR_STOLEN: Int = 57
    const val REQUEST_SET_LIMITS: Int = 58

    const val EVENT_ADD_PHOTO: Int = 6
    const val EVENT_CHOOSE_PHOTO: Int = 7
    const val EVENT_REMOVE_PHOTO: Int = 8

    const val EVENT_ADD_TOPUP_CARD: Int = 100
    const val EVENT_DELETE_TOPUP_CARD: Int = 101

    const val REQUEST_CODE = "requestCode"
    const val START_REQUEST_CODE = 101
    const val INVALID_OLD_PIN: String = "PT3101"
}