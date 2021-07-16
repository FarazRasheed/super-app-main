package co.yap.modules.dashboard.more.bankdetails.viewmodel

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.bankdetails.interfaces.IBankDetail
import co.yap.modules.dashboard.more.bankdetails.states.BankDetailStates
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.maskAccountNumber
import co.yap.yapcore.helpers.extentions.maskIbanNumber
import co.yap.yapcore.managers.SessionManager

class BankDetailViewModel(application: Application) : BaseViewModel<IBankDetail.State>(application),
    IBankDetail.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: BankDetailStates = BankDetailStates()

    init {
        SessionManager.user?.accountNo?.let { state.account.set(it.maskAccountNumber()) }
        SessionManager.user?.bank?.address?.let { state.addresse.set(it) }
        SessionManager.user?.bank?.name?.let { state.bank.set(it) }
        SessionManager.user?.iban?.let { state.iban.set(it.maskIbanNumber()) }
        SessionManager.user?.bank?.swiftCode?.let { state.swift.set(it) }

        state.name.set(SessionManager.user?.currentCustomer?.getFullName())
        state.toolbarTitle = getString(R.string.screen_more_detail_display_text_bank_details)
        SessionManager.user?.currentCustomer?.getPicture()?.let {
            state.image.set(it)
        }
        state.name.get()?.let { state.initials.set(Utils.shortName(it)) }
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

}