package co.yap.sendmoney.fundtransfer.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import co.yap.sendmoney.fundtransfer.interfaces.IBeneficiaryFundTransfer
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseState

class BeneficiaryFundTransferState : BaseState(), IBeneficiaryFundTransfer.State {

    override var toolbarVisibility: ObservableBoolean = ObservableBoolean()
    override var rightIcon: ObservableBoolean = ObservableBoolean()
    override var leftIcon: ObservableBoolean = ObservableBoolean()

    @get:Bindable
    override var rightButtonText: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightButtonText)
        }

    @get:Bindable
    override var position: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.position)
        }
}