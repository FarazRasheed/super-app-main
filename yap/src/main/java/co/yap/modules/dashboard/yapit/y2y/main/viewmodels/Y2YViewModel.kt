package co.yap.modules.dashboard.yapit.y2y.main.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.yapit.y2y.main.interfaces.IY2Y
import co.yap.modules.dashboard.yapit.y2y.main.states.Y2YState
import co.yap.networking.customers.requestdtos.Contact
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.managers.SessionManager

class Y2YViewModel(application: Application) : BaseViewModel<IY2Y.State>(application),
    IY2Y.ViewModel {

    override val yapContactLiveData: MutableLiveData<List<Contact>> = MutableLiveData()
    override var isSearching: MutableLiveData<Boolean> = MutableLiveData(false)
    override val searchQuery: MutableLiveData<String> = MutableLiveData("")
    override var errorEvent: MutableLiveData<String> = MutableLiveData()
    override val state: Y2YState = Y2YState()

    override fun onCreate() {
        super.onCreate()
        SessionManager.getCurrenciesFromServer { _, _ -> }
    }

}