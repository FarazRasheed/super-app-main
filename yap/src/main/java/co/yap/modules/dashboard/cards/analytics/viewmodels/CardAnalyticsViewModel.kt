package co.yap.modules.dashboard.cards.analytics.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.modules.dashboard.cards.analytics.interfaces.ICardAnalytics
import co.yap.modules.dashboard.cards.analytics.main.viewmodels.CardAnalyticsBaseViewModel
import co.yap.modules.dashboard.cards.analytics.models.AnalyticsItem
import co.yap.modules.dashboard.cards.analytics.states.CardAnalyticsState
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.DateUtils.FORMAT_MONTH_YEAR
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager
import java.util.*

class CardAnalyticsViewModel(application: Application) :
    CardAnalyticsBaseViewModel<ICardAnalytics.State>(application = application),
    ICardAnalytics.ViewModel {
    override val state: CardAnalyticsState = CardAnalyticsState(application)
    override var selectedModel: MutableLiveData<AnalyticsItem> = MutableLiveData()
    val repository: TransactionsRepository = TransactionsRepository
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    var currentCalendar: Calendar = Calendar.getInstance()
    var creationCalender: Calendar = Calendar.getInstance()

    override fun onCreate() {
        super.onCreate()
        setToolBarTitle(getString(Strings.screen_card_analytics_tool_bar_title))
        DateUtils.dateToString(currentCalendar.time, "yyyy-MM-dd")
        state.nextMonth = false
        SessionManager.user?.creationDate?.let {str ->
            val date =
                DateUtils.stringToDate(
                    str,
                    DateUtils.SERVER_DATE_FORMAT
                )
            state.selectedMonth = DateUtils.dateToString(currentCalendar.time, FORMAT_MONTH_YEAR)
            parentViewModel?.state?.currentSelectedMonth = state.selectedMonth ?: ""
            parentViewModel?.state?.currentSelectedDate = DateUtils.dateToString(currentCalendar.time,"yyyy-MM-dd")

            date?.let { dates ->
                creationCalender.time = dates
                if (creationCalender.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
                    state.previousMonth = false
                } else {
                    if (creationCalender.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH)) {
                        state.previousMonth = true
                    }
                }
            }

        }
    }

    override fun handlePressOnView(id: Int) {
        when (id) {
            R.id.ivPrevious -> {

                if ((currentCalendar.get(Calendar.MONTH) - 1) > creationCalender.get(Calendar.MONTH)) {
                    currentCalendar.add(Calendar.MONTH, -1)
                    state.nextMonth = true
                } else if ((currentCalendar.get(Calendar.MONTH) - 1) == creationCalender.get(
                        Calendar.MONTH
                    )
                ) {
                    currentCalendar.add(Calendar.MONTH, -1)
                    state.previousMonth = false
                    // // Proper testing remaining
                    state.nextMonth = true
                }
                state.selectedMonth =
                    DateUtils.dateToString(currentCalendar.time, FORMAT_MONTH_YEAR)
                fetchCardCategoryAnalytics(
                    DateUtils.dateToString(
                        currentCalendar.time,
                        "yyyy-MM-dd"
                    )
                )
                parentViewModel?.state?.currentSelectedMonth = state.selectedMonth ?: ""
                parentViewModel?.state?.currentSelectedDate = DateUtils.dateToString(currentCalendar.time,"yyyy-MM-dd")
            }
            R.id.ivNext -> {
                val tempCalendar = Calendar.getInstance()
                if ((tempCalendar.get(Calendar.MONTH) - 1) > (currentCalendar.get(Calendar.MONTH))) {
                    currentCalendar.add(Calendar.MONTH, 1)
                    state.previousMonth = true
                } else if ((tempCalendar.get(Calendar.MONTH) - 1) == currentCalendar.get(Calendar.MONTH)) {
                    currentCalendar.add(Calendar.MONTH, 1)
                    state.nextMonth = false
                    // Proper testing remaining
                    state.previousMonth = true
                }
                state.selectedMonth =
                    DateUtils.dateToString(currentCalendar.time, FORMAT_MONTH_YEAR)
                fetchCardCategoryAnalytics(
                    DateUtils.dateToString(
                        currentCalendar.time,
                        "yyyy-MM-dd"
                    )
                )
                parentViewModel?.state?.currentSelectedMonth = state.selectedMonth ?: ""
                parentViewModel?.state?.currentSelectedDate = DateUtils.dateToString(currentCalendar.time,"yyyy-MM-dd")
            }
        }
        clickEvent.setValue(id)
    }

    override fun fetchCardCategoryAnalytics(currentMonth: String) {
        val categoryList = ArrayList<TxnAnalytic>()
        //call api here
        launch {
            state.loading = true
            when (val response = repository.getAnalyticsByCategoryName(
                SessionManager.getCardSerialNumber(), currentMonth
            )) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let {analyticsDTO ->
                        state.monthlyCategoryAvgAmount =
                            response.data.data?.monthlyAvgAmount?.toString()

                        state.totalCategorySpent = response.data.data?.totalTxnAmount.toString()
                            .toFormattedCurrency(
                                showCurrency = true,
                                currency = state.currencyType ?: SessionManager.getDefaultCurrency()
                            )
                        state.totalSpent = state.totalCategorySpent
                        clickEvent.postValue(Constants.CATEGORY_AVERAGE_AMOUNT_VALUE)
                        parentViewModel?.categoryAnalyticsItemLiveData?.value = analyticsDTO.txnAnalytics
                    }

                    fetchCardMerchantAnalytics(currentMonth)

                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message

                }
            }
        }

    }

    override fun fetchCardMerchantAnalytics(currentMonth: String) {
        launch {
            when (val response = repository.getAnalyticsByMerchantName(
                SessionManager.getCardSerialNumber(), currentMonth
            )) {
                is RetroApiResponse.Success -> {
                    state.monthlyMerchantAvgAmount =
                        response.data.data?.monthlyAvgAmount?.toString()
                    state.totalMerchantSpent = response.data.data?.totalTxnAmount.toString()
                        .toFormattedCurrency(
                            showCurrency = true,
                            currency = state.currencyType ?: SessionManager.getDefaultCurrency()
                        )

                    parentViewModel?.merchantAnalyticsItemLiveData?.value =
                        response.data.data?.txnAnalytics
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
            //state.loading = false
        }
        /*merchantList.add(
            TxnAnalytic(
                "https://yap-live.s3.eu-west-1.amazonaws.com/amazon.png",
                "Amazon",
                "887.12",
                20.00,
                24
            )
        )
        merchantList.add(
            TxnAnalytic(
                "https://yap-live.s3.eu-west-1.amazonaws.com/amazon.png",
                "Amazon",
                "887.12",
                20.00,
                24
            )
        )
        merchantList.add(
            TxnAnalytic(
                "https://yap-live.s3.eu-west-1.amazonaws.com/amazon.png",
                "Amazon",
                "887.12",
                20.00,
                24
            )
        )
        merchantList.add(
            TxnAnalytic(
                "https://yap-live.s3.eu-west-1.amazonaws.com/amazon.png",
                "Amazon",
                "887.12",
                20.00,
                24
            )
        )
        merchantList.add(
                TxnAnalytic(
                    "https://yap-live.s3.eu-west-1.amazonaws.com/amazon.png",
                    "Amazon",
                    "887.12",
                    20.00,
                    24
                )
                )


           parentVM?.merchantAnalyticsItemLiveData?.value = merchantList*/

    }
}