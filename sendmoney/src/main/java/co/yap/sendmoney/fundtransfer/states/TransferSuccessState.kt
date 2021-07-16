package co.yap.sendmoney.fundtransfer.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.sendmoney.fundtransfer.interfaces.ITransferSuccess
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseState

class TransferSuccessState : BaseState(), ITransferSuccess.State {

    override var cutOffMessage: ObservableField<String> = ObservableField()
    @get:Bindable
    override var successHeader: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.successHeader)
        }

    @get:Bindable
    override var flagLayoutVisibility: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.flagLayoutVisibility)
        }

    @get:Bindable
    override var transferAmountHeading: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferAmountHeading)
        }
    @get:Bindable
    override var buttonTitle: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }
    @get:Bindable
    override var availableBalanceString: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalanceString)
        }

}