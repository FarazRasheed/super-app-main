package co.yap.modules.dashboard.cards.addpaymentcard.spare.states

import androidx.databinding.Bindable
import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.ICardBenefit
import co.yap.BR
import co.yap.yapcore.BaseState

class CardBenefitState : BaseState(), ICardBenefit.State {

    @get:Bindable
    override var benefitsModel: BenefitsModel? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.benefitsModel)
        }
}