package co.yap.modules.setcardpin.activities

import android.app.Application
import co.yap.modules.setcardpin.interfaces.ISetCardPinWelcomeActivity
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class SetPinChildViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {

    var parentViewModel: ISetCardPinWelcomeActivity.ViewModel? = null
}