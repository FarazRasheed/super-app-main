package co.yap.yapcore.helpers.extentions

import android.content.Context
import co.yap.networking.customers.responsedtos.AccountInfo
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.enums.*
import co.yap.yapcore.managers.SessionManager

fun AccountInfo.getUserAccessRestrictions(completion: (ArrayList<UserAccessRestriction>) -> Unit = {}): ArrayList<UserAccessRestriction> {
    val restrictions: ArrayList<UserAccessRestriction> = arrayListOf()

    if (partnerBankStatus?.equals(PartnerBankStatus.ACTIVATED.status) == false) {
        restrictions.add(UserAccessRestriction.ACCOUNT_INACTIVE)
    }

    restrictions.add(
        when (this.freezeInitiator) {
            "MOBILE_APP_HOSTLIST" -> {
                UserAccessRestriction.CARD_HOTLISTED_BY_APP
            }
            "CUSTOMER_REQUEST" -> {
                UserAccessRestriction.CARD_HOTLISTED_BY_CSR
            }
            "BANK_REQUEST" -> {
                when (this.severityLevel) {
                    AccountBlockSeverityLevel.TOTAL_BLOCK.freezeCode -> {
                        UserAccessRestriction.IBAN_BLOCKED_BY_RAK_TOTAL
                    }
                    AccountBlockSeverityLevel.DEBIT_BLOCK.freezeCode -> {
                        UserAccessRestriction.IBAN_BLOCKED_BY_RAK_DEBIT
                    }
                    AccountBlockSeverityLevel.CREDIT_BLOCK.freezeCode -> {
                        UserAccessRestriction.IBAN_BLCOKED_BY_RAK_CREDIT
                    }
                    else -> UserAccessRestriction.NONE
                }
            }
            "MASTER_CARD_REQUEST" -> {
                UserAccessRestriction.CARD_BLOCKED_BY_MASTER_CARD
            }
            "YAP_COMPLIANCE_TOTAL" -> {
                UserAccessRestriction.CARD_BLOCKED_BY_YAP_TOTAL
            }
            "YAP_COMPLIANCE_DEBIT" -> {
                UserAccessRestriction.CARD_BLOCKED_BY_YAP_DEBIT
            }
            "YAP_COMPLIANCE_CREDIT" -> {
                UserAccessRestriction.CARD_BLOCKED_BY_YAP_CREDIT
            }
            "EID_EXPIRED_SCHEDULER" -> {
                UserAccessRestriction.EID_EXPIRED
            }
            else -> UserAccessRestriction.NONE
        }
    )
    if (otpBlocked == true) {
        restrictions.add(UserAccessRestriction.OTP_BLOCKED)
    }

    if (SessionManager.card.value != null) {
        if (SessionManager.card.value?.status == CardStatus.PIN_BLOCKED.name) {
            restrictions.add(UserAccessRestriction.DEBIT_CARD_PIN_BLOCKED)
            completion.invoke(restrictions)
        }
    } else {
        SessionManager.getDebitCard { card ->
            if (card.status == CardStatus.PIN_BLOCKED.name) {
                restrictions.add(UserAccessRestriction.DEBIT_CARD_PIN_BLOCKED)
                completion.invoke(restrictions)
            }
        }
    }
    completion.invoke(restrictions)
    return restrictions
}

fun AccountInfo?.getBlockedFeaturesList(key: UserAccessRestriction): ArrayList<FeatureSet> {
    return (when (key) {
        UserAccessRestriction.CARD_FREEZE_BY_APP, UserAccessRestriction.CARD_FREEZE_BY_CSR -> {
            arrayListOf()
        }
        UserAccessRestriction.CARD_HOTLISTED_BY_APP, UserAccessRestriction.CARD_HOTLISTED_BY_CSR, UserAccessRestriction.CARD_BLOCKED_BY_MASTER_CARD -> {
            arrayListOf(
                FeatureSet.UNFREEZE_CARD,
                FeatureSet.CHANGE_PIN,
                FeatureSet.FORGOT_PIN
            )
        }

        UserAccessRestriction.IBAN_BLOCKED_BY_RAK_TOTAL -> {
            arrayListOf(
                FeatureSet.DOMESTIC_TRANSFER,
                FeatureSet.UAEFTS_TRANSFER,
                FeatureSet.RMT_TRANSFER,
                FeatureSet.SWIFT_TRANSFER,
                FeatureSet.CBWSI_TRANSFER,
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.TOP_UP_BY_EXTERNAL_CARD,
                FeatureSet.Y2Y_TRANSFER,
                FeatureSet.UNFREEZE_CARD
            )
        }
        UserAccessRestriction.IBAN_BLOCKED_BY_RAK_DEBIT -> {
            arrayListOf(
                FeatureSet.DOMESTIC_TRANSFER,
                FeatureSet.UAEFTS_TRANSFER,
                FeatureSet.RMT_TRANSFER,
                FeatureSet.SWIFT_TRANSFER,
                FeatureSet.CBWSI_TRANSFER,
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.Y2Y_TRANSFER,
                FeatureSet.UNFREEZE_CARD
            )
        }
        UserAccessRestriction.IBAN_BLCOKED_BY_RAK_CREDIT -> {
            arrayListOf(
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.TOP_UP_BY_EXTERNAL_CARD,
                FeatureSet.UNFREEZE_CARD
            )
        }
        UserAccessRestriction.CARD_BLOCKED_BY_YAP_TOTAL, UserAccessRestriction.EID_EXPIRED -> {
            arrayListOf(
                FeatureSet.DOMESTIC_TRANSFER,
                FeatureSet.UAEFTS_TRANSFER,
                FeatureSet.RMT_TRANSFER,
                FeatureSet.SWIFT_TRANSFER,
                FeatureSet.CBWSI_TRANSFER,
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.TOP_UP_BY_EXTERNAL_CARD,
                FeatureSet.Y2Y_TRANSFER,
                FeatureSet.UNFREEZE_CARD,
                FeatureSet.CHANGE_PIN,
                FeatureSet.FORGOT_PIN
            )
        }
        UserAccessRestriction.CARD_BLOCKED_BY_YAP_DEBIT -> {
            arrayListOf(
                FeatureSet.DOMESTIC_TRANSFER,
                FeatureSet.UAEFTS_TRANSFER,
                FeatureSet.RMT_TRANSFER,
                FeatureSet.SWIFT_TRANSFER,
                FeatureSet.CBWSI_TRANSFER,
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.Y2Y_TRANSFER,
                FeatureSet.UNFREEZE_CARD,
                FeatureSet.CHANGE_PIN,
                FeatureSet.FORGOT_PIN
            )
        }
        UserAccessRestriction.CARD_BLOCKED_BY_YAP_CREDIT -> {
            arrayListOf(
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.UNFREEZE_CARD,
                FeatureSet.TOP_UP_BY_EXTERNAL_CARD,
                FeatureSet.CHANGE_PIN,
                FeatureSet.FORGOT_PIN
            )
        }
        UserAccessRestriction.OTP_BLOCKED -> {
            arrayListOf(
                FeatureSet.DOMESTIC_TRANSFER,
                FeatureSet.UAEFTS_TRANSFER,
                FeatureSet.RMT_TRANSFER,
                FeatureSet.SWIFT_TRANSFER,
                FeatureSet.CBWSI_TRANSFER,
                FeatureSet.ADD_FUNDS,
                FeatureSet.REMOVE_FUNDS,
                FeatureSet.TOP_UP_BY_EXTERNAL_CARD,
                FeatureSet.Y2Y_TRANSFER,
                FeatureSet.CHANGE_PIN,
                FeatureSet.FORGOT_PIN,
                FeatureSet.CHANGE_PASSCODE,
                FeatureSet.FORGOT_PASSCODE,
                FeatureSet.ADD_SEND_MONEY_BENEFICIARY,
                FeatureSet.EDIT_SEND_MONEY_BENEFICIARY,
                FeatureSet.EDIT_EMAIL,
                FeatureSet.EDIT_PHONE_NUMBER,
                FeatureSet.DELETE_SEND_MONEY_BENEFICIARY,
                FeatureSet.UPDATE_EID
            )
        }
        UserAccessRestriction.ACCOUNT_INACTIVE -> {
            arrayListOf(FeatureSet.SEND_MONEY, FeatureSet.YAP_TO_YAP, FeatureSet.TOP_UP)
        }
        UserAccessRestriction.DEBIT_CARD_PIN_BLOCKED -> {
            arrayListOf(FeatureSet.CHANGE_PIN, FeatureSet.FORGOT_PIN)
        }
        UserAccessRestriction.NONE -> {
            arrayListOf()
        }
    })
}

fun AccountInfo.getBlockedMessage(key: UserAccessRestriction, context: Context): String {
    return (when (key) {
        UserAccessRestriction.EID_EXPIRED, UserAccessRestriction.CARD_FREEZE_BY_APP, UserAccessRestriction.CARD_FREEZE_BY_CSR,
        UserAccessRestriction.CARD_HOTLISTED_BY_APP, UserAccessRestriction.CARD_HOTLISTED_BY_CSR, UserAccessRestriction.IBAN_BLOCKED_BY_RAK_TOTAL
            , UserAccessRestriction.IBAN_BLOCKED_BY_RAK_DEBIT, UserAccessRestriction.IBAN_BLCOKED_BY_RAK_CREDIT, UserAccessRestriction.CARD_BLOCKED_BY_MASTER_CARD
            , UserAccessRestriction.CARD_BLOCKED_BY_YAP_TOTAL, UserAccessRestriction.CARD_BLOCKED_BY_YAP_DEBIT, UserAccessRestriction.CARD_BLOCKED_BY_YAP_CREDIT -> {

            Translator.getString(
                context,
                Strings.common_display_text_feature_blocked_error
            ).format(SessionManager.helpPhoneNumber)

        }
        UserAccessRestriction.OTP_BLOCKED -> {
            Translator.getString(context, Strings.screen_blocked_otp_display_text_message).format(
                SessionManager.helpPhoneNumber
            )
        }
        UserAccessRestriction.ACCOUNT_INACTIVE -> {
            Translator.getString(
                context,
                Strings.screen_popup_activation_pending_display_text_message
            )
        }
        else -> {
            Translator.getString(
                context,
                Strings.common_display_text_feature_blocked_error
            ).format(SessionManager.helpPhoneNumber)
        }
    })
}

fun AccountInfo.getNotificationOfBlockedFeature(
    key: UserAccessRestriction,
    context: Context
): String? {
    return (when (key) {
        UserAccessRestriction.CARD_FREEZE_BY_APP, UserAccessRestriction.CARD_FREEZE_BY_CSR,
        UserAccessRestriction.CARD_HOTLISTED_BY_APP, UserAccessRestriction.CARD_HOTLISTED_BY_CSR, UserAccessRestriction.IBAN_BLOCKED_BY_RAK_TOTAL
            , UserAccessRestriction.IBAN_BLOCKED_BY_RAK_DEBIT, UserAccessRestriction.IBAN_BLCOKED_BY_RAK_CREDIT, UserAccessRestriction.CARD_BLOCKED_BY_MASTER_CARD
            , UserAccessRestriction.CARD_BLOCKED_BY_YAP_TOTAL, UserAccessRestriction.CARD_BLOCKED_BY_YAP_DEBIT, UserAccessRestriction.CARD_BLOCKED_BY_YAP_CREDIT -> {

            Translator.getString(
                context,
                Strings.iban_or_debit_card_freeze_or_blocked_message
            ).format(SessionManager.helpPhoneNumber)

        }
        else -> null
    })
}
