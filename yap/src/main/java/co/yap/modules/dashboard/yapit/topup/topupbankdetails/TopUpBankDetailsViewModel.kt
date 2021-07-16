package co.yap.modules.dashboard.yapit.topup.topupbankdetails

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class TopUpBankDetailsViewModel(application: Application) :
    BaseViewModel<ITopUpBankDetails.State>(application = application), ITopUpBankDetails.ViewModel {
    override val state: TopUpBankDetailsState = TopUpBankDetailsState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override fun onCreate() {
        super.onCreate()
        state.toolbarTitle = "My YAP account details"
    }

    override fun handlePressOnButton(id: Int) {
        clickEvent.setValue(id)
    }
}