package co.yap.household.onboard.cardselection

import androidx.recyclerview.widget.RecyclerView
import co.yap.household.databinding.ItemHouseHoldCardBinding
import co.yap.household.onboard.onboarding.viewmodels.HouseHoldCardSelectionItemViewModel
import co.yap.networking.customers.responsedtos.HouseHoldCardsDesign
import co.yap.yapcore.helpers.extentions.loadImage
import co.yap.yapcore.interfaces.OnItemClickListener

class HouseHoldCardSelectionItemViewHolder(private val itemHouseHoldCardBinding: ItemHouseHoldCardBinding) :
    RecyclerView.ViewHolder(itemHouseHoldCardBinding.root) {
    fun onBind(
        houseHoldCardsDesignModel: HouseHoldCardsDesign,
        position: Int,
        dimensions: IntArray,
        onItemClickListener: OnItemClickListener?
    ) {
        val params = itemHouseHoldCardBinding.ivCard.layoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        itemHouseHoldCardBinding.ivCard.layoutParams = params

        itemHouseHoldCardBinding.ivCard.loadImage(houseHoldCardsDesignModel.frontSideDesignImage?:"")
        itemHouseHoldCardBinding.houseHoldCardItemViewModel?.position = position
        itemHouseHoldCardBinding.houseHoldCardItemViewModel =
            HouseHoldCardSelectionItemViewModel(
                position,
                houseHoldCardsDesignModel,
                onItemClickListener
            )
        itemHouseHoldCardBinding.executePendingBindings()
    }
}