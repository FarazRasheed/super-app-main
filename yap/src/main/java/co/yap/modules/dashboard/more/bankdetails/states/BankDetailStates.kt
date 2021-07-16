package co.yap.modules.dashboard.more.bankdetails.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.bankdetails.interfaces.IBankDetail
import co.yap.yapcore.BaseState

class BankDetailStates : BaseState(), IBankDetail.State {
    override var name: ObservableField<String> = ObservableField()
    override var swift: ObservableField<String> = ObservableField("")
    override var iban: ObservableField<String> = ObservableField("")
    override var account: ObservableField<String> = ObservableField("")
    override var bank: ObservableField<String> = ObservableField("")
    override var addresse: ObservableField<String> = ObservableField("")
    override var image: ObservableField<String> = ObservableField("")
    override var initials: ObservableField<String> = ObservableField()
}