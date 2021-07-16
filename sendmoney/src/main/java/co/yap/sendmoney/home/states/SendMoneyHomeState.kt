package co.yap.sendmoney.home.states

import androidx.databinding.ObservableField
import co.yap.sendmoney.home.interfaces.ISendMoneyHome
import co.yap.yapcore.BaseState

class SendMoneyHomeState : BaseState(), ISendMoneyHome.State {
    override var isNoBeneficiary: ObservableField<Boolean> = ObservableField(false)
    override var isSearching: ObservableField<Boolean> = ObservableField(false)
    override var hasBeneficiary: ObservableField<Boolean> = ObservableField(false)
    override var flagDrawableResId: ObservableField<Int> = ObservableField(-1)
    override var isNoRecentBeneficiary: ObservableField<Boolean> = ObservableField(true)
}