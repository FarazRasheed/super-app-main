package co.yap.yapcore.constants

import co.yap.app.YAPApplication

object Constants {
    const val addCard = "addCard"
    const val isPinCreated = "isPinCreated"

    const val CHANGE_PASSCODE: String = "CHANGE_PASSCODE"
    const val TRANSACTION_ID: String = "TRANSACTION_ID"
    const val CARD_SERIAL_NUMBER: String = "CARD_SERIAL_NUMBER"
    const val IS_TOPUP_SKIP: String = "isTopUpSkip"
    const val FORGOT_PASSCODE_FROM_CHANGE_PASSCODE: String = "forgotPasscodeFromChangePasscodeFlow"
    const val FORGOT_CARD_PIN_FLOW: String = "FORGOT_CARD_PIN_FLOW"

    const val LONGITUDE = "longitude"
    const val LATITUDE = "latitude"
    const val SUCCESS_RESULT = "SUCCESS_RESULT"
    const val LOCATION_ADDRESS = "LOCATION_ADDRESS"
    const val ERROR_MESSAGE = "ERROR_MESSAGE"

    const val CARD: String = "CARD"
    const val KEY: String = "Key"
    const val TYPE: String = "type"
    const val TYPE_ADD_CARD: String = "TYPE_ADD_CARD"
    const val TYPE_TOP_UP_TRANSACTION: String = "TYPE_TOP_UP_TRANSACTION"
    const val START_POOLING: String = "START_POOLING"
    const val TOP_UP_VIA_EXTERNAL_CARD: String = "TOP_UP_VIA_CARD"

    const val THEME_YAP: String = "CORE"
    const val THEME_HOUSEHOLD: String = "HOUSEHOLD"

    const val MANUAL_DEBIT = "DEBIT"
    const val MANUAL_CREDIT = "CREDIT"
    const val FEE_TYPE_FLAT = "FLAT"

    //More Option Constants
    const val MORE_NOTIFICATION: Int = 1
    const val MORE_LOCATE_ATM: Int = 2
    const val MORE_INVITE_FRIEND: Int = 3
    const val MORE_HELP_SUPPORT: Int = 4
    const val EVENT_CREATE_CARD_PIN: Int = 13

    //Add Note flow constants
    const val INTENT_ADD_NOTE_REQUEST = 2222
    const val KEY_NOTE_VALUE = "noteValue"

    // Invite Friend Constants
    const val URL_SHARE_APP_STORE = "itms-apps://itunes.apple.com/app/id1024941703"
    val URL_SHARE_PLAY_STORE =
        "https://play.google.com/store/apps/details?id=${YAPApplication.configManager?.applicationId}"

    const val MODE_STATUS_SCREEN: Int = 1
    const val MODE_HELP_SUPPORT: Int = 2
    const val MODE_MEETING_CONFORMATION: Int = 12
    const val FORGOT_CARD_PIN_NAVIGATION: Int = 1

    const val TOP_UP_TRANSACTION_SUCCESS: Int = 6

    //CardAnalytics constants
    const val CATEGORY_AVERAGE_AMOUNT_VALUE: Int = 7
    const val MERCHANT_AVERAGE_AMOUNT_VALUE: Int = 8

    const val URL_YAP_WEBSITE = "https://www.yap.com/"
    const val URL_TERMS_CONDITION = "https://www.yap.com/terms"
    const val URL_DISCLAIMER_TERMS = "https://www.yap.com/terms/transfers"
    const val URL_FEES_AND_PRICING_PLAN = URL_TERMS_CONDITION


    //HTML Key
    const val URL_TOP_UP_TRANSACTION_HTML = "URL_TOP_UP_TRANSACTION_HTML"

    //Other events
    const val EVENT_TOP_UP_CARD_TRANSACTION: Int = 5
    const val REQUEST_CODE_CREATE_PASSCODE: Int = 127

    // Cash pickup flow constants
    const val BENEFICIARY: String = "Beneficiary"
    const val POSITION: String = "Position"
    const val IS_NEW_BENEFICIARY: String = "IS_NEW_BENEFICIARY"

    const val ADD_CASH_PICK_UP_SUCCESS = 10001
    const val ADD_SUCCESS = 10004


    //Core Payment Card Types
    const val VISA = "Visa"
    const val MASTER = "Mastercard"
    const val JCB = "JCB"
    const val DINNERS = "Diners Club"
    const val AMEX = "American Express"
    const val DISCOVER = "Discover"

    const val USER_STATUS_CARD_ACTIVATED: String = "CARD_ACTIVATED"

    const val BROADCAST_UPDATE_TRANSACTION: String = "BROADCAST_UPDATE_TRANSACTION"

    // Money Transfer
    const val MONEY_TRANSFERED = "MONEY_TRANSFERED"
    const val BENEFICIARY_CHANGE = "BENEFICIARY_CHANGE"
    const val IS_TRANSFER_MONEY = "IS_TRANSFER_MONEY"
    const val TERMINATE_ADD_BENEFICIARY = "TERMINATE_ADD_BENEFICIARY"

    //Location Selection
    const val ADDRESS = "address"
    const val ADDRESS_SUCCESS = "address_success"
    const val PLACES_PHOTO_ID = "places_photo_id"

    // Events for ViewState

    const val EVENT_LOADING: Int = 1111
    const val EVENT_EMPTY: Int = 2222
    const val EVENT_CONTENT: Int = 3333
    const val EVENT_ERROR: Int = 4444

    //Notifications Actions

    const val NOTIFICATION_ACTION_SET_PIN: String = "SET_PIN"
    const val NOTIFICATION_ACTION_COMPLETE_VERIFICATION: String = "COMPLETE_VERIFICATION"

    const val USER_STATUS_ON_BOARDED: String = "ON_BOARDED"
    const val USER_STATUS_MEETING_SCHEDULED: String = "MEETING_SCHEDULED"
    const val USER_STATUS_MEETING_SUCCESS: String = "MEETING_SUCCESS"

    const val name = "name"
    const val data = "payLoad"
    const val result = "result"
    const val skipped = "skipped"
    val FRAGMENT_CLASS = "fragment_class"
    val SHOW_TOOLBAR = "_show_toolbar"
    val EXTRA = "_bundle_extras"
    const val OVERVIEW_BENEFICIARY = "overview_beneficiary"
    const val IS_IBAN_NEEDED = "is_iban_need"

    // SharedPreference  Keys

    const val KEY_APP_UUID = "KEY_APP_UUID"
    const val KEY_PASSCODE: String = "PASSCODE"
    const val KEY_USERNAME: String = "USEERNAME"
    const val KEY_TOUCH_ID_ENABLED: String = "TOUCH_ID_ENABLED"
    const val KEY_IS_USER_LOGGED_IN: String = "KEY_IS_USER_LOGGED_IN"
    const val KEY_IS_FIRST_TIME_USER: String = "KEY_IS_FIRST_TIME_USER"
    const val KEY_IS_FINGERPRINT_PERMISSION_SHOWN: String =
        "KEY_IS_FINGERPRINT_PERMISSION_SHOWN"
    const val KEY_AVAILABLE_BALANCE: String = "AVAILABLE_BALANCE"
    const val KEY_THEME = "KEY_THEME"
    const val VERIFY_PASS_CODE_BTN_TEXT = "verify_pass_code_btn_text"
    const val KEY_IS_REMEMBER = "IS_REMEMBER"

    const val PAGE_URL = "_page_url"
    const val TOOLBAR_TITLE = "toolbar_title"

    const val REFERRAL_TIME = "time"
    const val REFERRAL_ID = "customer_id"

    const val CURRENCYWALLET = "MultiCurrencyWallet"

    //Sms consent
    const val SMS_CONSENT_REQUEST = 2

    // Add Money Constants
    const val ADD_MONEY_TOP_UP_VIA_CARD: Int = 1
    const val ADD_MONEY_SAMSUNG_PAY: Int = 2
    const val ADD_MONEY_GOOGLE_PAY: Int = 3
    const val ADD_MONEY_BANK_TRANSFER: Int = 4
    const val ADD_MONEY_CASH_OR_CHEQUE: Int = 5
    const val ADD_MONEY_QR_CODE: Int = 6

    //Analytics
    const val TRANSACTION_DETAIL = "TxnAnalytics_Model"
    const val TRANSACTION_POSITION = "Transaction_Item_Position"
    var MERCHANT_TYPE: String = "merchant-name"

    //ATM/CDM
    const val LOCATION_TYPE = "ATM_OR_CDM"
    const val LOCATION_ATM = "ATM"
    const val LOCATION_CDM = "CDM"

}
