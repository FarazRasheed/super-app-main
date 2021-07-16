package co.yap.modules.dashboard.cards.status.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.status.interfaces.IYapCardStatus
import co.yap.modules.dashboard.cards.status.state.YapCardStatusState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class YapCardStatusViewModel(application: Application) :
    BaseViewModel<IYapCardStatus.State>(application),
    IYapCardStatus.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: YapCardStatusState = YapCardStatusState()
    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }
}