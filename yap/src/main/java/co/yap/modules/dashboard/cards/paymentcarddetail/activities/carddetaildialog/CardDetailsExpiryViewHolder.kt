package co.yap.modules.dashboard.cards.paymentcarddetail.activities.carddetaildialog

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.DialogCardDetailsCardExpiryBinding

class CardDetailsExpiryViewHolder(private val dialogCardDetailsCardExpiryBinding: DialogCardDetailsCardExpiryBinding) :
    RecyclerView.ViewHolder(dialogCardDetailsCardExpiryBinding.root) {
    fun onBind(
        cardDetailsModel: CardDetailsModel,
        position: Int
    ) {
        /*val params = dialogCardDetailsCardSerialNumberBinding.ivCard.layoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        dialogCardDetailsCardSerialNumberBinding.ivCard.layoutParams = params*/

        //dialogCardDetailsCardSerialNumberBinding.ivCard.loadImage(houseHoldCardsDesignModel.frontSideDesignImage?:"")
        dialogCardDetailsCardExpiryBinding.tvCardType.text = cardDetailsModel.cardType
        dialogCardDetailsCardExpiryBinding.tvCardValidityValue.text =
            cardDetailsModel.cardExpiry
        dialogCardDetailsCardExpiryBinding.tvCvvValue.text =
            cardDetailsModel.cardCvv
        dialogCardDetailsCardExpiryBinding.tvCardName.text =
            cardDetailsModel.displayName
        dialogCardDetailsCardExpiryBinding.viewModel?.position = position
        dialogCardDetailsCardExpiryBinding.viewModel =
            CardDetailsDialogItemViewModel(
                position,
                cardDetailsModel
            )
        dialogCardDetailsCardExpiryBinding.executePendingBindings()
    }
}