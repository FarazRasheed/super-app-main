package co.yap.modules.dashboard.cards.analytics.states

import android.app.Application
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.analytics.interfaces.ICardAnalytics
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class CardAnalyticsState(application: Application) : BaseState(), ICardAnalytics.State {
    val context = application.applicationContext

    @get:Bindable
    override var previousMonth: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.previousMonth)
        }

    @get:Bindable
    override var nextMonth: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.nextMonth)
        }

    @get:Bindable
    override var selectedMonth: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedMonth)
        }
    @get:Bindable
    override var monthCount: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthCount)
        }

    @get:Bindable
    override var monthlyAverageString: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthlyAverageString)
        }
    @get:Bindable
    override var monthlyMerchantAverageString: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthlyMerchantAverageString)
        }
    @get:Bindable
    override var currencyType: String? = SessionManager.getDefaultCurrency()
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyType)
        }
    @get:Bindable
    override var monthlyCategoryAvgAmount: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthlyCategoryAvgAmount)
        }
    @get:Bindable
    override var monthlyMerchantAvgAmount: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.monthlyMerchantAvgAmount)
        }

    @get:Bindable
    override var selectedItemSpentValue: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedItemSpentValue)
        }
    @get:Bindable
    override var selectedItemPercentage: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedItemPercentage)
        }
    @get:Bindable
    override var selectedItemName: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedItemName)
        }
    @get:Bindable
    override var selectedItemPosition: Int = -1
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedItemPosition)
        }
    @get:Bindable
    override var totalSpent: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalSpent)
        }
    @get:Bindable
    override var totalCategorySpent: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalCategorySpent)
        }
    @get:Bindable
    override var totalMerchantSpent: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.totalMerchantSpent)
        }

    @get:Bindable
    override var selectedTxnAnalyticsItem: TxnAnalytic? = TxnAnalytic("", "", "", 0.0, 0)
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedTxnAnalyticsItem)
        }

    fun setUpString(currencyType: String?, amount: String?) {
        monthlyAverageString =
            Translator.getString(context, Strings.screen_card_analytics_display_month_average_text)
                .format(currencyType, amount)
    }

    fun setUpStringForMerchant(currencyType: String?, amount: String?) {
        monthlyMerchantAverageString =
            Translator.getString(context, Strings.screen_card_analytics_display_month_average_text)
                .format(currencyType, amount)
    }
}