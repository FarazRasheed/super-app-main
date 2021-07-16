package co.yap.household.onboard.onboarding.viewmodels

import android.view.View
import co.yap.networking.customers.responsedtos.HouseHoldCardsDesign
import co.yap.yapcore.interfaces.OnItemClickListener

class HouseHoldCardSelectionItemViewModel(
    var position: Int?,
    var houseHoldCardsDesignModel: HouseHoldCardsDesign,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnClick(view: View) {
        onItemClickListener?.onItemClick(view, houseHoldCardsDesignModel, position ?: 0)
    }
}