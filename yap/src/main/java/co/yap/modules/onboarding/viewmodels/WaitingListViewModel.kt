package co.yap.modules.onboarding.viewmodels

import android.app.Application
import co.yap.modules.onboarding.interfaces.IWaitingList
import co.yap.modules.onboarding.states.WaitingListState
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class WaitingListViewModel(application: Application) :
    OnboardingChildViewModel<IWaitingList.State>(application), IWaitingList.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override val state: WaitingListState = WaitingListState()

    override fun onCreate() {
        state.rankNoInList?.value =
            getString(Strings.screen_waiting_list_display_text_heading).format(
                parentViewModel?.rankNo?.value ?: ""
            )
        super.onCreate()
    }

    override fun onResume() {
        super.onResume()
        setProgress(100)
    }

    override fun handlePressOnNext(id: Int) {
        clickEvent.setValue(id)
    }
}
