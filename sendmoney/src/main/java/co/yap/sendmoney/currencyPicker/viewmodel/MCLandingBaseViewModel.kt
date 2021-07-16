package co.yap.sendmoney.currencyPicker.viewmodel

import android.app.Application
import co.yap.sendmoney.currencyPicker.interfaces.IMCLanding
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class MCLandingBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IMCLanding.ViewModel? = null
}