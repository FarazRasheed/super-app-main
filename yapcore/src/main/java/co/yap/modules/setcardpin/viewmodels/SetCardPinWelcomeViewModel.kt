package co.yap.modules.setcardpin.viewmodels

import android.app.Application
import co.yap.modules.setcardpin.activities.SetPinChildViewModel
import co.yap.modules.setcardpin.interfaces.ISetCardPinWelcome
import co.yap.modules.setcardpin.states.SetCardPinWelcomeState
import co.yap.yapcore.SingleClickEvent

class SetCardPinWelcomeViewModel(application: Application) :
    SetPinChildViewModel<ISetCardPinWelcome.State>(application),
    ISetCardPinWelcome.ViewModel {

    override val state: SetCardPinWelcomeState = SetCardPinWelcomeState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnCreatePin(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnCreatePinLater(id: Int) {
        clickEvent.setValue(id)
    }
}