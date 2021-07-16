package co.yap.modules.dashboard.yapit.addmoney.landing

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.yapit.addmoney.main.AddMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants

class AddMoneyLandingViewModel(application: Application) :
    AddMoneyBaseViewModel<IAddMoneyLanding.State>(application),
    IAddMoneyLanding.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: IAddMoneyLanding.State =
        AddMoneyLandingState()
    override val landingAdapter =
        AddMoneyLandingAdapter(
            context,
            mutableListOf()
        )

    override fun onCreate() {
        super.onCreate()
        setToolBarTitle(getString(Strings.screen_fragment_yap_it_add_money_title))
        landingAdapter.setList(getAddMoneyOptions())
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getAddMoneyOptions(): MutableList<AddMoneyLandingOptions> {
        val list = mutableListOf<AddMoneyLandingOptions>()
        list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_TOP_UP_VIA_CARD,
                getString(Strings.screen_fragment_yap_it_add_money_text_top_via_card),
                R.drawable.ic_icon_card_transfer
            )
        )
      /*  list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_GOOGLE_PAY,
                getString(Strings.screen_fragment_yap_it_add_money_text_google_pay),
                R.drawable.flag_ad
            )
        )
        list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_SAMSUNG_PAY,
                getString(Strings.screen_fragment_yap_it_add_money_text_samsung_pay),
                R.drawable.flag_ae
            )
        )*/
        list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_BANK_TRANSFER,
                getString(Strings.screen_fragment_yap_it_add_money_text_bank_transfer),
                R.drawable.ic_bank_transfer
            )
        )
        list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_CASH_OR_CHEQUE,
                getString(Strings.screen_fragment_yap_it_add_money_text_cash_or_cheque),
                R.drawable.ic_cash
            )
        )
        list.add(
            AddMoneyLandingOptions(
                Constants.ADD_MONEY_QR_CODE,
                getString(Strings.screen_fragment_yap_it_add_money_text_qr_code),
                R.drawable.ic_qr_code
            )
        )
        return list
    }
}