package co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces

import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICardBenefit {
    interface State : IBase.State {
        var benefitsModel: BenefitsModel?
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
    }

    interface View : IBase.View<ViewModel>
}