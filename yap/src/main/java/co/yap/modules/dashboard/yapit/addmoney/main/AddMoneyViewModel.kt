package co.yap.modules.dashboard.yapit.addmoney.main

import android.app.Application
import co.yap.yapcore.BaseViewModel

class AddMoneyViewModel(application: Application) :
    BaseViewModel<IAddMoney.State>(application = application),
    IAddMoney.ViewModel {
    override val state: AddMoneyState =
        AddMoneyState()
}