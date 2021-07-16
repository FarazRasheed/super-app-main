package co.yap.modules.dashboard.cards.analytics.main.interfaces

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICardAnalyticsMain {

    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        val categoryAnalyticsItemLiveData: MutableLiveData<ArrayList<TxnAnalytic>>
        val merchantAnalyticsItemLiveData: MutableLiveData<ArrayList<TxnAnalytic>>
        val selectedItemPosition: MutableLiveData<Int>
        val selectedItemPositionParent: MutableLiveData<Int>
        val isMerchant: MutableLiveData<Boolean>
    }

    interface State : IBase.State {
        var leftButtonVisibility: ObservableBoolean
        var toolbarVisibility: ObservableBoolean
        var currentSelectedMonth : String
        var currentSelectedDate : String
    }

}