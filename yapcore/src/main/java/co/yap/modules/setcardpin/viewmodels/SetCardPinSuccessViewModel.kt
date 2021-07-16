package co.yap.modules.setcardpin.viewmodels

import android.app.Application
import co.yap.modules.setcardpin.activities.SetPinChildViewModel
import co.yap.modules.setcardpin.interfaces.ISetCardPinSuccess
import co.yap.modules.setcardpin.states.SetCardPinSuccessState
import co.yap.yapcore.AdjustEvents.Companion.trackAdjustPlatformEvent
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.adjust.AdjustEvents

class SetCardPinSuccessViewModel(application: Application) :
    SetPinChildViewModel<ISetCardPinSuccess.State>(application),
    ISetCardPinSuccess.ViewModel {

    override val state: SetCardPinSuccessState = SetCardPinSuccessState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        trackAdjustPlatformEvent(AdjustEvents.SET_PIN_END.type)
    }

    override fun handlePressOnTopUp(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnTopUpLater(id: Int) {
        clickEvent.setValue(id)
    }
}