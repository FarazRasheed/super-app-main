package co.yap.modules.dashboard.cards.analytics.main.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.cards.analytics.main.interfaces.ICardAnalyticsMain
import co.yap.modules.dashboard.cards.analytics.main.states.CardAnalyticsMainState
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class CardAnalyticsMainViewModel(application: Application) :
    BaseViewModel<ICardAnalyticsMain.State>(application),
    ICardAnalyticsMain.ViewModel {
    override val state: CardAnalyticsMainState =
        CardAnalyticsMainState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val categoryAnalyticsItemLiveData: MutableLiveData<ArrayList<TxnAnalytic>> =
        MutableLiveData()
    override val merchantAnalyticsItemLiveData: MutableLiveData<ArrayList<TxnAnalytic>> =
        MutableLiveData()
    override val selectedItemPosition: MutableLiveData<Int> = MutableLiveData()
    override fun onCreate() {
        super.onCreate()
        state.toolbarVisibility.set(true)
        state.leftButtonVisibility.set(true)
    }
    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override val selectedItemPositionParent: MutableLiveData<Int> = MutableLiveData()
    override val isMerchant: MutableLiveData<Boolean> = MutableLiveData(false)
}