package co.yap.modules.onboarding.viewmodels

import android.app.Application
import co.yap.modules.onboarding.interfaces.IMeetingConfirmation
import co.yap.modules.onboarding.states.MeetingConfirmationState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent

class MeetingConfirmationViewModel(application: Application) : BaseViewModel<IMeetingConfirmation.State>(application),
    IMeetingConfirmation.viewModel {

    override val goToDashboardButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: MeetingConfirmationState = MeetingConfirmationState()

    override fun handlePressOnGoToDashboard() {
        goToDashboardButtonPressEvent.value = true
    }
}
