package co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IChangeCardPinSuccess
import co.yap.modules.dashboard.cards.paymentcarddetail.states.ChangeCardPinSuccessState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class ChangeCardPinSuccessViewModel(application: Application) :
    BaseViewModel<IChangeCardPinSuccess.State>(application),
    IChangeCardPinSuccess.ViewModel {
    override val state: ChangeCardPinSuccessState = ChangeCardPinSuccessState()

    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnButtonClick(id: Int) {
        clickEvent.postValue(id)
    }

}