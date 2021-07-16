package co.yap.modules.dashboard.yapit.addmoney.main

import androidx.databinding.ObservableBoolean
import co.yap.yapcore.BaseState

class AddMoneyState : BaseState(),
    IAddMoney.State {
    override var toolBarVisibility: ObservableBoolean? = ObservableBoolean(false)
    override var toolBarRightIconVisibility: ObservableBoolean? =ObservableBoolean(false)

}