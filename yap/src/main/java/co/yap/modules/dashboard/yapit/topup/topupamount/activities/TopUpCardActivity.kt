package co.yap.modules.dashboard.yapit.topup.topupamount.activities

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.networking.customers.responsedtos.beneficiary.TopUpTransactionModel
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.defaults.DefaultActivity
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.IBaseNavigator

class TopUpCardActivity : DefaultActivity(), INavigator, IFragmentHolder {

    var cardInfo: TopUpCard? = null
    var topUpTransactionModel: MutableLiveData<TopUpTransactionModel>? = MutableLiveData()
    var successButtonLabel: String = ""

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this, R.id.card_top_up_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up_card)
        cardInfo =
            (intent?.getValue(Constants.CARD, ExtraType.PARCEABLE.name) as? TopUpCard)
        successButtonLabel =
            (intent?.getValue("successButtonLabel", ExtraType.STRING.name) as? String) ?: ""
    }
}