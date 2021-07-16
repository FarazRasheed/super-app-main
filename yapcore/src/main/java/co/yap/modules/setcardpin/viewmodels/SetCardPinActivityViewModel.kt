package co.yap.modules.setcardpin.viewmodels

import android.app.Application
import co.yap.modules.setcardpin.interfaces.ISetCardPinWelcomeActivity
import co.yap.modules.setcardpin.states.SetCardPinWelcomeActivityState
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseViewModel

class SetCardPinActivityViewModel(application: Application) :
    BaseViewModel<ISetCardPinWelcomeActivity.State>(application),
    ISetCardPinWelcomeActivity.ViewModel {

    override lateinit var card: Card
    override var skipWelcome: Boolean = false
    override val state: SetCardPinWelcomeActivityState = SetCardPinWelcomeActivityState()

}