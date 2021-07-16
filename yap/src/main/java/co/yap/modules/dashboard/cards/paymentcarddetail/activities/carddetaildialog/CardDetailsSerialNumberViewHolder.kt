package co.yap.modules.dashboard.cards.paymentcarddetail.activities.carddetaildialog

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.DialogCardDetailsCardSerialNumberBinding
import co.yap.yapcore.helpers.extentions.toCamelCase

class CardDetailsSerialNumberViewHolder(private val dialogCardDetailsCardSerialNumberBinding: DialogCardDetailsCardSerialNumberBinding) :
    RecyclerView.ViewHolder(dialogCardDetailsCardSerialNumberBinding.root) {
    fun onBind(
        cardDetailsModel: CardDetailsModel,
        position: Int
    ) {
        /*val params = dialogCardDetailsCardSerialNumberBinding.ivCard.layoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        dialogCardDetailsCardSerialNumberBinding.ivCard.layoutParams = params*/

        //dialogCardDetailsCardSerialNumberBinding.ivCard.loadImage(houseHoldCardsDesignModel.frontSideDesignImage?:"")
        dialogCardDetailsCardSerialNumberBinding.tvCardType.text = cardDetailsModel.cardType
        dialogCardDetailsCardSerialNumberBinding.tvCardNumberValue.text =
            cardDetailsModel.cardNumber?.toCamelCase()
        dialogCardDetailsCardSerialNumberBinding.tvCardName.text =
            cardDetailsModel.displayName
        dialogCardDetailsCardSerialNumberBinding.viewModel?.position = position
        dialogCardDetailsCardSerialNumberBinding.viewModel =
            CardDetailsDialogItemViewModel(
                position,
                cardDetailsModel
            )
        dialogCardDetailsCardSerialNumberBinding.executePendingBindings()
    }
}