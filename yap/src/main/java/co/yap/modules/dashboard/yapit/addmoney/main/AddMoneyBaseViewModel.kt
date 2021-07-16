package co.yap.modules.dashboard.yapit.addmoney.main

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class AddMoneyBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IAddMoney.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolBarVisibility?.set(visibility)
    }

    fun toolBarRightIconVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolBarRightIconVisibility?.set(visibility)
    }
}