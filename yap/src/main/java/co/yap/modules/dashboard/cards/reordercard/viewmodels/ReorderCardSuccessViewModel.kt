package co.yap.modules.dashboard.cards.reordercard.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.reordercard.interfaces.IReorderCardSuccess
import co.yap.modules.dashboard.cards.reordercard.states.ReorderCardSuccessState
import co.yap.yapcore.SingleClickEvent

class ReorderCardSuccessViewModel(application: Application) :
    ReorderCardBaseViewModel<IReorderCardSuccess.State>(application),
    IReorderCardSuccess.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override val state: IReorderCardSuccess.State = ReorderCardSuccessState()

    override fun onResume() {
        super.onResume()
        parentViewModel?.state?.toolbarVisibility?.set(false)
    }

}