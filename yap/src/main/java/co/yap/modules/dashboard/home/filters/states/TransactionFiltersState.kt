package co.yap.modules.dashboard.home.filters.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.home.filters.interfaces.ITransactionFilters
import co.yap.yapcore.BaseState

class TransactionFiltersState : BaseState(), ITransactionFilters.State {
    override var rangeStartValue: ObservableField<String> = ObservableField()
    override var rangeEndValue: ObservableField<String> = ObservableField()
    override var selectedStartRange: ObservableField<Double> = ObservableField(0.00)
    override var selectedEndRange: ObservableField<Double> = ObservableField(0.00)
    override var selectedTxnType: ObservableField<String?> = ObservableField()
    override var hasInternet: ObservableField<Boolean> = ObservableField(false)
}