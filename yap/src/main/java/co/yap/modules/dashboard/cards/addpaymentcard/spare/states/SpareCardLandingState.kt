package co.yap.modules.dashboard.cards.addpaymentcard.spare.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.addpaymentcard.interfaces.ISpareCards
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class SpareCardLandingState : BaseState(), ISpareCards.State{


    @get:Bindable
    override var virtualCardFee: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.virtualCardFee)
        }

    @get:Bindable
    override var physicalCardFee: String = "${SessionManager.getDefaultCurrency()} 0.0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardFee)
        }


}