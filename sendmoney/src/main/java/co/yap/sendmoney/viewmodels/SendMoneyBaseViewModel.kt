package co.yap.sendmoney.viewmodels

import android.app.Application
import co.yap.sendmoney.interfaces.ISendMoney
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class SendMoneyBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: ISendMoney.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }
}