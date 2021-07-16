package co.yap.modules.dashboard.cards.addpaymentcard.spare.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.ICardBenefit
import co.yap.modules.dashboard.cards.addpaymentcard.spare.states.CardBenefitState
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentChildViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class CardBenfitsDetailViewModel(application: Application) :
    AddPaymentChildViewModel<ICardBenefit.State>(application), ICardBenefit.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: CardBenefitState =
        CardBenefitState()


    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_spare_card_benefit_display_text_title))
    }

}