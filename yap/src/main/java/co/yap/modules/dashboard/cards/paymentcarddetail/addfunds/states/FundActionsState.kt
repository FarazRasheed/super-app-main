package co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.states

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces.IFundActions
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.extentions.toFormattedCurrency

class FundActionsState(application: Application) : BaseState(), IFundActions.State {
    var context: Context = application.applicationContext

    @get:Bindable
    override var cardNumber: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardNumber)
        }
    override var isAddFundScreen: ObservableField<Boolean> = ObservableField()

    override var cardInfo: ObservableField<TopUpCard> = ObservableField(TopUpCard())

    @get:Bindable
    override var cardName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardName)
        }

    @get:Bindable
    override var enterAmountHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.enterAmountHeading)
        }

    @get:Bindable
    override var currencyType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyType)
        }

    @get:Bindable
    override var denominationFirstAmount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.denominationFirstAmount)
        }

    @get:Bindable
    override var denominationSecondAmount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.denominationSecondAmount)
        }

    @get:Bindable
    override var denominationThirdAmount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.denominationThirdAmount)
        }

    @get:Bindable
    override var availableBalanceGuide: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalanceGuide)
        }

    @get:Bindable
    override var availableBalance: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalance)
        }

    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }

    @get:Bindable
    override var amount: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.amount)
            clearError()


        }

    @get:Bindable
    override var denominationAmount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.denominationAmount)
            clearError()
        }


    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var maxLimit: Double = 0.00
        set(value) {
            field = value
            notifyPropertyChanged(BR.maxLimit)
        }

    @get:Bindable
    override var minLimit: Double = 0.00
        set(value) {
            field = value
            notifyPropertyChanged(BR.minLimit)
        }

    @get:Bindable
    override var amountBackground: Drawable? =
        context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
        set(value) {
            field = value
            notifyPropertyChanged(BR.amountBackground)
        }

    @get:Bindable
    override var errorDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorDescription)
        }

    @get:Bindable
    override var availableBalanceText: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalanceText)
        }

    @get:Bindable
    override var topUpSuccess: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.topUpSuccess)
        }


    @get:Bindable
    override var primaryCardUpdatedBalance: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.primaryCardUpdatedBalance)
        }

    @get:Bindable
    override var spareCardUpdatedBalance: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.spareCardUpdatedBalance)
        }

    @get:Bindable
    override var transactionFeeSpannableString: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transactionFeeSpannableString)
        }

    @get:Bindable
    override var transferFee: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferFee)
        }


    fun checkValidityForAddTopUpFromExternalCard(): String {
        try {
            if (amount != "") {
                when {
                    amount?.toDoubleOrNull() ?: 0.0 > maxLimit -> {
                        amountBackground =
                            context.resources.getDrawable(
                                co.yap.yapcore.R.drawable.bg_funds_error,
                                null
                            )

                        errorDescription = Translator.getString(
                            context,
                            Strings.screen_add_funds_display_text_max_limit_error,
                            currencyType,
                            maxLimit.toString()
                                .toFormattedCurrency(showCurrency = false, currency = currencyType)
                                ?: ""
                        )
                        return errorDescription

                    }
                    else -> {
                        amountBackground =
                            context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
                    }
                }
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }

    private fun clearError() {
        if (amount != "") {
            if (amount != ".") {
                valid = amount?.toDouble()!! >= minLimit
                amountBackground =
                    context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
            }
        } else if (amount == "") {
            valid = false
        }
    }

}