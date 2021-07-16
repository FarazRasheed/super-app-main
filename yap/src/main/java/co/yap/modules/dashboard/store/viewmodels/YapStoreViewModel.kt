package co.yap.modules.dashboard.store.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.modules.dashboard.store.interfaces.IYapStore
import co.yap.modules.dashboard.store.states.YapStoreState
import co.yap.networking.store.responsedtos.Store
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import kotlinx.coroutines.delay

class YapStoreViewModel(application: Application) : BaseViewModel<IYapStore.State>(application),
    IYapStore.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: YapStoreState = YapStoreState()
    override val storesLiveData: MutableLiveData<MutableList<Store>> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        state.toolbarVisibility.set(true)
        state.rightIconVisibility.set(true)
        state.toolbarTitle = getString(Strings.screen_yap_store_display_text_title)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getStoreList() {
        // need api in future
        val list = mutableListOf<Store>()
        state.loading = true
        launch {
            delay(1000)
            list.add(
                Store(
                    1,
                    "YAP Young",
                    "Open a bank account for your children and help empower them financially.",
                    R.drawable.ic_store_young, R.drawable.ic_young_smile
                )
            )
            list.add(
                Store(
                    2,
                    "YAP Household",
                    "Manage your household salaries digitally.",
                    R.drawable.ic_store_household, R.drawable.ic_young_household
                )
            )
            storesLiveData.value = list
            state.loading = false
        }
    }
}