package co.yap.modules.dashboard.yapit.topup.landing

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class TopUpLandingViewModel(application: Application) :
    BaseViewModel<ITopUpLanding.State>(application),
    ITopUpLanding.ViewModel {

    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: ITopUpLanding.State = TopUpLandingState()

    override fun handlePressOnBackButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)

    }
}