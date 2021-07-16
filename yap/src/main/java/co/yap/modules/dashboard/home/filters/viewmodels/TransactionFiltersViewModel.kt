package co.yap.modules.dashboard.home.filters.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.home.filters.interfaces.ITransactionFilters
import co.yap.modules.dashboard.home.filters.states.TransactionFiltersState
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.responsedtos.TransactionFilters
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.Utils
import com.jaygoo.widget.RangeSeekBar

class TransactionFiltersViewModel(application: Application) :
    BaseViewModel<ITransactionFilters.State>(application),
    ITransactionFilters.ViewModel, IRepositoryHolder<TransactionsRepository> {
    override val repository: TransactionsRepository = TransactionsRepository
    override val state: ITransactionFilters.State = TransactionFiltersState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val transactionFilters: MutableLiveData<TransactionFilters> = MutableLiveData()
    override var txnFilters: MutableLiveData<co.yap.modules.dashboard.home.filters.models.TransactionFilters> =
        MutableLiveData()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        launch {
            state.loading = true
            when (val response = repository.getSearchFilterAmount()) {
                is RetroApiResponse.Success -> transactionFilters.value = response.data.data
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.hasInternet.set(response.error.statusCode == 504)
                }
            }
            state.loading = false
        }
    }

    override fun updateRangeValue(seekBar: RangeSeekBar) {

        val startRangeValue =
            Utils.getFormattedCurrencyWithoutDecimal(seekBar.minProgress.toString())
        val endRangeValues =
            Utils.getFormattedCurrencyWithoutDecimal(seekBar.leftSeekBar.progress.toString())
        state.rangeStartValue.set(startRangeValue)
        state.rangeEndValue.set(endRangeValues)
    }

}