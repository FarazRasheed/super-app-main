package co.yap.modules.dashboard.yapit.addmoney.qrcode

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.managers.SessionManager

class QRCodeViewModel(application: Application) :
    BaseViewModel<IQRCode.State>(application),
    IQRCode.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: QRCodeState = QRCodeState()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        populateState()
    }

    private fun populateState() {
        SessionManager.user?.let {
            state.fullName = it.currentCustomer.getFullName()
            it.currentCustomer.getPicture().let { picture ->
                state.profilePictureUrl = picture
            }
        }
    }

}