package co.yap.modules.dashboard.cards.reportcard.interfaces

import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent

interface IReportStolenActivity {

    interface State : IBase.State {
        var tootlBarVisibility: Int
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnTickButton()
        var card: Card?
    }

    interface View : IBase.View<ViewModel>
}