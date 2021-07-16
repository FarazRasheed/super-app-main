package co.yap.sendmoney.fundtransfer.states

import androidx.databinding.Bindable
import co.yap.sendmoney.BR
import co.yap.sendmoney.fundtransfer.interfaces.IInternationalTransactionConfirmation
import co.yap.yapcore.BaseState

class InternationalTransactionConfirmationState : BaseState(),
    IInternationalTransactionConfirmation.State {

    @get:Bindable
    override var transferDescription: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferDescription)
        }
    @get:Bindable
    override var confirmHeading: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmHeading)
        }
    @get:Bindable
    override var receivingAmountDescription: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.receivingAmountDescription)
        }
    @get:Bindable
    override var transferFeeDescription: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferFeeDescription)
        }

    @get:Bindable
    override var cutOffTimeMsg: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.cutOffTimeMsg)
        }
}