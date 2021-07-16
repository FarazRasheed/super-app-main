package co.yap.modules.setcardpin.interfaces

import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase

interface ISetCardPinWelcomeActivity {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var card: Card
        var skipWelcome: Boolean
    }

    interface State : IBase.State
}