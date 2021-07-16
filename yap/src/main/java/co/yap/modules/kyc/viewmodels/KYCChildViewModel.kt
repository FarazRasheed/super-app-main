package co.yap.modules.kyc.viewmodels

import android.app.Application
import co.yap.modules.kyc.interfaces.IDocumentsDashboard
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class KYCChildViewModel<S : IBase.State>(application: Application) : BaseViewModel<S>(application) {

    var parentViewModel: IDocumentsDashboard.ViewModel? = null

}