package co.yap.sendmoney.fundtransfer.states

import androidx.databinding.ObservableField
import co.yap.sendmoney.fundtransfer.interfaces.ICashTransferConfirmation
import co.yap.yapcore.BaseState

class CashTransferConfirmationState : BaseState(), ICashTransferConfirmation.State {
    override var description: ObservableField<CharSequence> =
        ObservableField()
    override var cutOffTimeMsg: ObservableField<String> = ObservableField()
    override var productCode: ObservableField<String> = ObservableField()
    override var transferFeeDescription: ObservableField<CharSequence> = ObservableField()
}