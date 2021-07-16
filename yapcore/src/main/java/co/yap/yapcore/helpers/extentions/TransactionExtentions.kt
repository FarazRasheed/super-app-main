package co.yap.yapcore.helpers.extentions

import android.text.format.DateFormat
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.yapcore.R
import co.yap.yapcore.enums.TransactionLabelsCode
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.enums.TransactionStatus
import co.yap.yapcore.enums.TxnType
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.managers.SessionManager
import java.util.*

fun Transaction?.getTransactionTitle(): String {
    this?.let { transaction -> // poo4 poo6
        return (when (transaction.productCode) {
            TransactionProductCode.Y2Y_TRANSFER.pCode -> {
                String.format(
                    "%s %s",
                    if (transaction.txnType == TxnType.DEBIT.type) "To" else "From",
                    if (transaction.txnType == TxnType.DEBIT.type) transaction.receiverName
                        ?: transaction.title else transaction.senderName
                        ?: transaction.title
                )
            }
            TransactionProductCode.TOP_UP_VIA_CARD.pCode -> {
                transaction.maskedCardNo?.let {
                    String.format(
                        "%s %s",
                        "Money added via",
                        "*" + it.substring(it.length - 4, it.length)
                    )
                } ?: transaction.title ?: "Unknown"

            }
            TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode -> "Withdraw from Virtual Card"
            TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode -> "Add to Virtual Card"

            else -> transaction.title ?: "Unknown"
        })
    } ?: return "Unknown"
}

fun Transaction?.getTransactionIcon(): Int {
    return this?.let { transaction ->
        return when (transaction.status) {
            TransactionStatus.CANCELLED.name, TransactionStatus.FAILED.name -> {
                when (transaction.productCode) {
                    TransactionProductCode.POS_PURCHASE.pCode -> R.drawable.ic_reverted
                    TransactionProductCode.TOP_UP_VIA_CARD.pCode -> R.drawable.ic_reverted
                    TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode -> R.drawable.ic_reverted
                    else -> -1
                }
            }
            else -> when (transaction.getLabelValues()) {
                TransactionLabelsCode.IS_CASH -> R.drawable.ic_transaction_cash
                TransactionLabelsCode.IS_BANK -> R.drawable.ic_transaction_bank
                TransactionLabelsCode.IS_TRANSACTION_FEE -> R.drawable.ic_package_standered
                TransactionLabelsCode.IS_REFUND -> R.drawable.ic_refund
                TransactionLabelsCode.IS_INCOMING -> R.drawable.ic_plus_transactions
                else -> {
                    when {
                        TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode == transaction.productCode ?: "" || TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode == transaction.productCode ?: "" -> {
                            R.drawable.ic_transfer
                        }
                        TransactionProductCode.TOP_UP_VIA_CARD.pCode == transaction.productCode || TransactionProductCode.CASH_DEPOSIT_AT_RAK.pCode == transaction.productCode || TransactionProductCode.CHEQUE_DEPOSIT_AT_RAK.pCode == transaction.productCode -> {
                            R.drawable.ic_plus_transactions
                        }
                        TransactionProductCode.ATM_WITHDRAWL.pCode == transaction.productCode || TransactionProductCode.MASTER_CARD_ATM_WITHDRAWAL.pCode == transaction.productCode -> {
                            R.drawable.ic_cash_out_trasaction
                        }
                        TransactionProductCode.POS_PURCHASE.pCode == transaction.productCode -> {
                            getMerchantCategoryIcon()
                        }

                        else -> -1
                    }
                }
            }
        }
    } ?: 0
}

fun Transaction?.getTransactionStatus(): String {
    when (this?.productCode) {
        TransactionProductCode.ATM_WITHDRAWL.pCode -> return this.cardAcceptorLocation
            ?: ""
        TransactionProductCode.FUND_LOAD.pCode -> return this.otherBankName ?: ""
        else -> this?.let { txn ->
            return (when (txn.status) {
                TransactionStatus.CANCELLED.name, TransactionStatus.FAILED.name -> "Rejected transaction"
                TransactionStatus.PENDING.name, TransactionStatus.IN_PROGRESS.name -> {
                    if (txn.getLabelValues() != TransactionLabelsCode.IS_TRANSACTION_FEE) "Transaction in progress" else ""
                }
                else -> ""
            })
        } ?: return ""
    }
}

fun Transaction?.getTransactionTypeTitle(): String {
    this?.let { txn ->
        return when {
            txn.getLabelValues() == TransactionLabelsCode.IS_TRANSACTION_FEE -> "Fee"
            txn.getLabelValues() == TransactionLabelsCode.IS_REFUND -> "Refund"
            txn.getLabelValues() == TransactionLabelsCode.IS_INCOMING -> "Inward Bank Transfer"
            TransactionProductCode.Y2Y_TRANSFER.pCode == txn.productCode -> "YTY"
            TransactionProductCode.TOP_UP_VIA_CARD.pCode == txn.productCode -> "Add money"
            TransactionProductCode.CASH_DEPOSIT_AT_RAK.pCode == txn.productCode || TransactionProductCode.CHEQUE_DEPOSIT_AT_RAK.pCode == txn.productCode || TransactionProductCode.ATM_DEPOSIT.pCode == txn.productCode || TransactionProductCode.FUND_LOAD.pCode == txn.productCode -> {
                if (txn.category.equals("REVERSAL", true)) "Reversal" else "Deposit"
            }
            TransactionProductCode.ATM_WITHDRAWL.pCode == txn.productCode || TransactionProductCode.MASTER_CARD_ATM_WITHDRAWAL.pCode == txn.productCode -> {
                if (txn.category.equals("REVERSAL", true)) "Reversal" else "Withdraw money"
            }
            TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode == txn.productCode -> {
                "Money moved"
            }
            TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode == txn.productCode -> {
                "Money moved"
            }
            else -> return (when (txn.productCode) {
                TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.CASH_PAYOUT.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.UAEFTS.pCode -> {
                    "Send money"
                }
                else ->
                    "Transaction"
            })
        }
    } ?: return "Transaction"
}

fun Transaction?.getTransactionTypeIcon(): Int {
    this?.let { transaction ->
        if (transaction.isTransactionRejected() || transaction.getLabelValues() == TransactionLabelsCode.IS_TRANSACTION_FEE) return android.R.color.transparent
        return if (isTransactionInProgress())
            R.drawable.ic_time
        else (when (txnType) {
            TxnType.DEBIT.type -> {
                when {
                    transaction.getLabelValues() == TransactionLabelsCode.IS_BANK -> R.drawable.ic_outgoing_transaction
                    productCode == TransactionProductCode.Y2Y_TRANSFER.pCode -> R.drawable.ic_outgoing_transaction_y2y
                    else -> android.R.color.transparent
                }
            }
            else -> android.R.color.transparent
        })
    } ?: return android.R.color.transparent
}

fun Transaction?.getCategoryIcon(): Int {
    this?.let { transaction ->
        if (transaction.getLabelValues() == TransactionLabelsCode.IS_TRANSACTION_FEE) {
            return R.drawable.ic_expense
        }
        return (when (transaction.productCode) {
            TransactionProductCode.Y2Y_TRANSFER.pCode -> R.drawable.ic_send_money
            TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode, TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode -> 0
            TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.CASH_PAYOUT.pCode -> {
                R.drawable.ic_send_money
            }
            TransactionProductCode.CARD_REORDER.pCode -> R.drawable.ic_expense
            TransactionProductCode.ATM_WITHDRAWL.pCode, TransactionProductCode.MASTER_CARD_ATM_WITHDRAWAL.pCode, TransactionProductCode.CASH_DEPOSIT_AT_RAK.pCode, TransactionProductCode.CHEQUE_DEPOSIT_AT_RAK.pCode, TransactionProductCode.INWARD_REMITTANCE.pCode, TransactionProductCode.LOCAL_INWARD_TRANSFER.pCode, TransactionProductCode.TOP_UP_VIA_CARD.pCode, TransactionProductCode.FUND_LOAD.pCode -> {
                R.drawable.ic_cash
            }
            TransactionProductCode.POS_PURCHASE.pCode -> getMerchantCategoryIcon()

            else -> 0
        })
    } ?: return 0
}

fun Transaction?.getCategoryTitle(): String {
    this?.let { transaction ->
        transaction.productCode?.let { productCode ->
            if (TransactionLabelsCode.IS_TRANSACTION_FEE == getLabelValues()) {
                return "Fee"
            }
            return (when (productCode) {
                TransactionProductCode.Y2Y_TRANSFER.pCode -> if (transaction.txnType == TxnType.DEBIT.type) "Outgoing Transfer" else "Incoming Transfer"
                TransactionProductCode.TOP_UP_VIA_CARD.pCode, TransactionProductCode.CASH_DEPOSIT_AT_RAK.pCode, TransactionProductCode.CHEQUE_DEPOSIT_AT_RAK.pCode, TransactionProductCode.INWARD_REMITTANCE.pCode, TransactionProductCode.LOCAL_INWARD_TRANSFER.pCode -> "Incoming Transfer"
                TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode, TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode -> ""
                TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.CASH_PAYOUT.pCode -> {
                    "Outgoing Transfer"
                }
                TransactionProductCode.CARD_REORDER.pCode -> "Fee"
                TransactionProductCode.FUND_LOAD.pCode -> "Incoming Funds"
                TransactionProductCode.POS_PURCHASE.pCode, TransactionProductCode.MASTER_CARD_ATM_WITHDRAWAL.pCode -> {
                    "Cash"
                }
                TransactionProductCode.ATM_WITHDRAWL.pCode -> {
                    if (transaction.category.equals("REVERSAL", true)) "Reversal" else "Cash"
                }
                else -> ""
            })
        } ?: return ""
    } ?: return ""
}

fun Transaction?.getMerchantCategoryIcon(): Int {
    this?.let { transaction ->
        return (when {
            transaction.merchantCategoryName.equals(
                "shopping",
                true
            ) -> R.drawable.ic_shopping_no_bg
            transaction.merchantCategoryName.equals(
                "education",
                true
            ) -> R.drawable.ic_education_no_bg
            transaction.merchantCategoryName.equals(
                "utilities",
                true
            ) -> R.drawable.ic_utilities_no_bg
            transaction.merchantCategoryName.equals(
                "healthAndBeauty",
                true
            ) -> R.drawable.ic_health_and_beauty_no_bg
            transaction.merchantCategoryName.equals(
                "Insurance",
                true
            ) -> R.drawable.ic_insurance_no_bg
            else -> R.drawable.ic_other_no_bg
        })
    } ?: return R.drawable.ic_other_no_bg
}

fun Transaction?.getMapImage(): Int {
    this?.let { transaction ->
        if (TransactionLabelsCode.IS_TRANSACTION_FEE == getLabelValues()) {
            return R.drawable.ic_image_light_red_background
        }
        return (when (transaction.productCode) {
            TransactionProductCode.Y2Y_TRANSFER.pCode -> R.drawable.ic_image_blue_background
            TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode, TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode -> R.drawable.ic_image_brown_background
            TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.CASH_PAYOUT.pCode, TransactionProductCode.TOP_UP_VIA_CARD.pCode, TransactionProductCode.INWARD_REMITTANCE.pCode, TransactionProductCode.LOCAL_INWARD_TRANSFER.pCode -> R.drawable.ic_image_light_blue_background
            TransactionProductCode.CARD_REORDER.pCode -> R.drawable.ic_image_light_red_background
            TransactionProductCode.POS_PURCHASE.pCode, TransactionProductCode.CASH_DEPOSIT_AT_RAK.pCode, TransactionProductCode.MASTER_CARD_ATM_WITHDRAWAL.pCode, TransactionProductCode.CHEQUE_DEPOSIT_AT_RAK.pCode, TransactionProductCode.FUND_LOAD.pCode, TransactionProductCode.ATM_WITHDRAWL.pCode, TransactionProductCode.ATM_DEPOSIT.pCode -> R.drawable.ic_map
            else -> -1
        })
    } ?: return -1
}

fun Transaction?.getSpentLabelText(): String {
    this?.let { transaction ->
        transaction.productCode?.let { productCode ->
            return (when (productCode) {
                TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode, TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode -> "Amount"
                else -> {
                    when (transaction.txnType) {
                        TxnType.CREDIT.type -> "Amount"
                        TxnType.DEBIT.type -> {
                            when (transaction.productCode) {
                                TransactionProductCode.Y2Y_TRANSFER.pCode, TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.CASH_PAYOUT.pCode -> {
                                    "Amount"
                                }
                                TransactionProductCode.SWIFT.pCode, TransactionProductCode.RMT.pCode -> {
                                    if (transaction.currency == SessionManager.getDefaultCurrency()) "Amount" else "Amount"
                                }
                                else -> "Amount"
                            }
                        }
                        else -> ""
                    }
                }
            })
        } ?: return ""
    } ?: return ""
}

fun Transaction?.getCurrency(): String {
    this?.let { transaction ->
        return (when (transaction.productCode) {
            TransactionProductCode.SWIFT.pCode, TransactionProductCode.RMT.pCode -> {
                transaction.currency.toString()
            }
            else -> transaction.currency.toString()
        })
    } ?: return SessionManager.getDefaultCurrency()
}

fun Transaction?.getLabelValues(): TransactionLabelsCode? {
    this?.productCode?.let { productCode ->
        return (when (productCode) {
            TransactionProductCode.MANUAL_ADJUSTMENT.pCode, TransactionProductCode.VIRTUAL_ISSUANCE_FEE.pCode, TransactionProductCode.FSS_FUNDS_WITHDRAWAL.pCode, TransactionProductCode.CARD_REORDER.pCode, TransactionProductCode.FEE_DEDUCT.pCode, TransactionProductCode.PHYSICAL_ISSUANCE_FEE.pCode, TransactionProductCode.BALANCE_INQUIRY.pCode, TransactionProductCode.PIN_CHANGE.pCode, TransactionProductCode.MINISTATEMENT.pCode, TransactionProductCode.ACCOUNT_STATUS_INQUIRY.pCode, TransactionProductCode.FSS_FEE_NOTIFICATION.pCode -> {
                TransactionLabelsCode.IS_TRANSACTION_FEE
            }
            TransactionProductCode.UAEFTS.pCode, TransactionProductCode.DOMESTIC.pCode, TransactionProductCode.RMT.pCode, TransactionProductCode.SWIFT.pCode, TransactionProductCode.PAYMENT_TRANSACTION.pCode, TransactionProductCode.MOTO.pCode, TransactionProductCode.ECOM.pCode, TransactionProductCode.ATM_DEPOSIT.pCode -> {
                TransactionLabelsCode.IS_BANK
            }
            TransactionProductCode.CASH_PAYOUT.pCode, TransactionProductCode.CASH_ADVANCE.pCode -> {
                TransactionLabelsCode.IS_CASH
            }
            TransactionProductCode.REFUND_MASTER_CARD.pCode, TransactionProductCode.REVERSAL_MASTER_CARD.pCode, TransactionProductCode.REVERSAL_OF_TXN_ON_FAILURE.pCode -> {
                TransactionLabelsCode.IS_REFUND
            }
            TransactionProductCode.FUND_LOAD.pCode, TransactionProductCode.LOCAL_INWARD_TRANSFER.pCode, TransactionProductCode.INWARD_REMITTANCE.pCode -> {
                TransactionLabelsCode.IS_INCOMING
            }
            else -> null
        })
    } ?: return null
}

fun Transaction?.getFormattedDate(): String? {
    this?.creationDate?.let {
        val date = DateUtils.convertServerDateToLocalDate(it)
        date?.let { convertedDate ->
            val smsTime: Calendar = Calendar.getInstance()
            smsTime.timeInMillis = convertedDate.time
            //smsTime.timeZone = TimeZone.getDefault()

            val now: Calendar = Calendar.getInstance()
            val timeFormatString = "MMMM dd"
            val dateTimeFormatString = "EEEE, MMMM d"
            return when {
                now.get(Calendar.DATE) === smsTime.get(Calendar.DATE) -> {
                    "Today, " + DateFormat.format(timeFormatString, smsTime)
                }
                now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) === 1 -> {
                    "Yesterday, " + DateFormat.format(timeFormatString, smsTime)
                }
                now.get(Calendar.YEAR) === smsTime.get(Calendar.YEAR) -> {
                    DateFormat.format(dateTimeFormatString, smsTime).toString()
                }
                else -> {
                    DateFormat.format(timeFormatString, smsTime).toString()
                }
            }
        } ?: return null
    } ?: return null
}

fun Transaction?.getFormattedTime(outputFormat: String = DateUtils.FORMAT_TIME_24H): String {
    return (when {
        DateUtils.reformatStringDate(
            this?.updatedDate ?: "",
            DateUtils.SERVER_DATE_FORMAT,
            outputFormat
        ).isBlank() -> DateUtils.reformatStringDate(
            this?.creationDate ?: "",
            DateUtils.SERVER_DATE_FORMAT,
            outputFormat
        )
        else -> DateUtils.reformatStringDate(
            this?.creationDate ?: "",
            DateUtils.SERVER_DATE_FORMAT,
            outputFormat
        )
    })
}

fun Transaction?.getTransactionNoteDate(outputFormat: String = DateUtils.FORMAT_TIME_24H): String {
    return (DateUtils.reformatStringDate(
        this?.transactionNoteDate ?: "",
        DateUtils.SERVER_DATE_FORMAT,
        outputFormat
    ))
}

fun Transaction?.isTransactionCancelled(): Boolean {
    return this?.status == TransactionStatus.CANCELLED.name || this?.status == TransactionStatus.FAILED.name
}

fun Transaction?.isTransactionInProgress(): Boolean {
    return (TransactionStatus.PENDING.name == this?.status || TransactionStatus.IN_PROGRESS.name == this?.status && this.getLabelValues() != TransactionLabelsCode.IS_TRANSACTION_FEE)
}

fun Transaction?.getTransactionAmountPrefix(): String {
    if (this?.productCode == TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode || this?.productCode == TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode || this?.productCode == TransactionProductCode.ATM_WITHDRAWL.pCode) {
        return ""
    } else {
        (return when (this?.txnType) {
            TxnType.DEBIT.type -> "-"
            TxnType.CREDIT.type -> "+"
            else -> ""
        })
    }
}

fun Transaction?.getTransactionAmount(): String? {
    (return when (this?.txnType) {
        TxnType.DEBIT.type -> this.totalAmount.toString().toFormattedCurrency(showCurrency = false,currency =this.currency?:SessionManager.getDefaultCurrency())
        TxnType.CREDIT.type -> this.amount.toString().toFormattedCurrency(showCurrency = false,currency =this.currency?:SessionManager.getDefaultCurrency())
        else -> ""
    })
}

fun Transaction?.getFormattedTransactionAmount(): String? {
    return String.format(
        "%s %s", this?.getTransactionAmountPrefix(),
        this?.getTransactionAmount()
    )
}

fun Transaction?.getTransactionAmountColor(): Int {
    if (this?.productCode == TransactionProductCode.WITHDRAW_SUPPLEMENTARY_CARD.pCode || this?.productCode == TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode) {
        return R.color.colorPrimaryDark
    } else {
        (return when (this?.txnType) {
            TxnType.DEBIT.type -> R.color.colorPrimaryDark
            TxnType.CREDIT.type -> {
                if (!this.isTransactionInProgress() && this.status != TransactionStatus.FAILED.name)
                    R.color.colorSecondaryGreen
                else
                    R.color.colorPrimaryDark
            }
            else -> R.color.colorPrimaryDark
        })
    }
}

fun Transaction?.isTransactionRejected(): Boolean {
    return (this?.status == TransactionStatus.CANCELLED.name || this?.status == TransactionStatus.FAILED.name)
}

fun Transaction?.showCutOffMsg(): Boolean {
    return (this?.productCode == TransactionProductCode.SWIFT.pCode)
}


