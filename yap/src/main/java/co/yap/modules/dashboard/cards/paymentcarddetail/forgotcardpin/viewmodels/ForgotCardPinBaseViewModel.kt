package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.interfaces.IForgotCardPin
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class ForgotCardPinBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IForgotCardPin.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolBarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolBarVisibility = visibility
    }
}