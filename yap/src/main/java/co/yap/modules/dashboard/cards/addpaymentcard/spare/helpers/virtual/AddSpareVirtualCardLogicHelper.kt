package co.yap.modules.dashboard.cards.addpaymentcard.spare.helpers.virtual

import android.content.Context
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.IAddSpareCard
import co.yap.modules.onboarding.models.TransactionModel

class AddSpareVirtualCardLogicHelper(
    val context: Context,
    val viewModel: IAddSpareCard.ViewModel
) {
    var transactionList: ArrayList<TransactionModel> = arrayListOf()

    init {
// work on api call and updating state values regarding virtual card view
    }
}