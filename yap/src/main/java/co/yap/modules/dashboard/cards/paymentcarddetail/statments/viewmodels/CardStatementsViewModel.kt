package co.yap.modules.dashboard.cards.paymentcarddetail.statments.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.adaptor.CardStatementsAdaptor
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.interfaces.ICardStatments
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.states.CardStatementsState
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.managers.SessionManager
import java.util.*

class CardStatementsViewModel(application: Application) :
    BaseViewModel<ICardStatments.State>(application),
    ICardStatments.ViewModel {

    private val transactionRepository: TransactionsRepository = TransactionsRepository
    override val state: CardStatementsState = CardStatementsState()
    override lateinit var card: Card
    override val adapter: ObservableField<CardStatementsAdaptor> = ObservableField()
    private val currentCalendar = Calendar.getInstance()
    private val creationCalender = Calendar.getInstance()
    private var minYear: Int = 0

    override fun onCreate() {
        super.onCreate()
        state.nextMonth = false
        state.year.set(currentCalendar.get(Calendar.YEAR).toString())
        SessionManager.user?.creationDate?.let { it ->
            val date =
                DateUtils.stringToDate(
                    it,
                    DateUtils.SERVER_DATE_FORMAT
                )
            date?.let {
                creationCalender.time = it
//                creationCalender.add(Calendar.YEAR ,-3)
                if (creationCalender.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                    state.previousMonth = false
                } else {
                    if (creationCalender.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)) {
                        state.previousMonth = true
                    }
                }
//                minYear = creationCalender.get(Calendar.YEAR)
            }
        }
    }

    override fun loadStatements(serialNumber: String) {
        launch {
            state.loading = true
            when (val response =
                transactionRepository.getCardStatements(serialNumber)) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let { it ->
                        state.statementList = it
                        filerDataByYear(state.year.get())
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun loadStatementsFromDashBoard() {
        launch {
            state.loading = true
            when (val response =
                transactionRepository.getAccountStatements()) {
                is RetroApiResponse.Success -> {
                    response.data.data?.let { it ->
                        state.statementList = it
                        filerDataByYear(state.year.get())
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }

    private fun filerDataByYear(year: String?) {
        state.statementList?.filter { it.year == year }?.let {
            adapter.get()?.setList(it.asReversed())
            state.hasRecords.set(it.isNotEmpty())
        }
    }

    fun onNext() {
        val tempCalendar = Calendar.getInstance()
        if ((tempCalendar.get(Calendar.YEAR) - 1) > (currentCalendar.get(Calendar.YEAR))) {
            currentCalendar.add(Calendar.YEAR, 1)
            state.previousMonth = true
        } else if ((tempCalendar.get(Calendar.YEAR) - 1) == currentCalendar.get(Calendar.YEAR)) {
            currentCalendar.add(Calendar.YEAR, 1)
            state.nextMonth = false
            // Proper testing remaining
            state.previousMonth = true
        }
        state.year.set(currentCalendar.get(Calendar.YEAR).toString())
        filerDataByYear(state.year.get())

    }

    fun onPrevious() {
        if ((currentCalendar.get(Calendar.YEAR) - 1) > creationCalender.get(Calendar.YEAR)) {
            currentCalendar.add(Calendar.YEAR, -1)
            state.nextMonth = true
        } else if ((currentCalendar.get(Calendar.YEAR) - 1) == creationCalender.get(Calendar.YEAR)) {
            currentCalendar.add(Calendar.YEAR, -1)
            state.previousMonth = false
            // // Proper testing remaining
            state.nextMonth = true
        }
        state.year.set(currentCalendar.get(Calendar.YEAR).toString())
        filerDataByYear(state.year.get())

//
//        if (minYear < currentCalendar.get(Calendar.YEAR)) {
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.YEAR, -1)
//            minYear = calendar.get(Calendar.YEAR)
//            state.year.set(calendar.get(Calendar.YEAR).toString())
//            filerDataByYear(state.year.get())
//        }
    }

}