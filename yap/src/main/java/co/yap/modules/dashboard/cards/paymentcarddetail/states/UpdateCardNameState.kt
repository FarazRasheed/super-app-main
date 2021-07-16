package co.yap.modules.dashboard.cards.paymentcarddetail.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IUpdateCardName
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.StringUtils

class UpdateCardNameState : BaseState(), IUpdateCardName.State {

    @get:Bindable
    override var cardName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardName)
            validateName()
        }
    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    private fun validateName() {
        valid = StringUtils.validateName(cardName)
    }
}