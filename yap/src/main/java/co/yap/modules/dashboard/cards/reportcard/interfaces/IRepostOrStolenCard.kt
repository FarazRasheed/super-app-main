package co.yap.modules.dashboard.cards.reportcard.interfaces

import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent

interface IRepostOrStolenCard {
    interface State : IBase.State {
        var cardType: String
        var maskedCardNumber: String
        var cautionMessage: String
        var valid: Boolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        val backButtonPressEvent: SingleLiveEvent<Boolean>
        val clickEvent: SingleClickEvent
        var HOT_LIST_REASON: Int
        val CARD_REORDER_SUCCESS: Int
        val cardFee: String
        fun handlePressOnBackButton()
        fun handlePressOnDamagedCard(id: Int)
        fun handlePressOnLostOrStolen(id: Int)
        fun handlePressOnReportAndBlockButton(id: Int)
        fun requestConfirmBlockCard(card: Card)
        fun getPhysicalCardFee()
        fun getDebitCardFee()
    }

    interface View : IBase.View<ViewModel>
}