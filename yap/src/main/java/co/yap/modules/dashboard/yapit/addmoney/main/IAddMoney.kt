package co.yap.modules.dashboard.yapit.addmoney.main

import androidx.databinding.ObservableBoolean
import co.yap.yapcore.IBase

interface IAddMoney {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State{
        var toolBarVisibility: ObservableBoolean?
        var toolBarRightIconVisibility: ObservableBoolean?
    }
}