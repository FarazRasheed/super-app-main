package co.yap.modules.dashboard.cards.home.viewholder

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemYapCardBinding
import co.yap.modules.dashboard.cards.home.viewmodels.YapCardItemViewModel
import co.yap.modules.others.helper.Constants
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.interfaces.OnItemClickListener


class YapCardItemViewHolder(private val itemYapCardBinding: ItemYapCardBinding) :
    RecyclerView.ViewHolder(itemYapCardBinding.root) {

    fun onBind(
        position: Int,
        paymentCard: Card?,
        dimensions: IntArray,
        onItemClickListener: OnItemClickListener?
    ) {

        val params = itemYapCardBinding.imgCard.layoutParams as ConstraintLayout.LayoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        itemYapCardBinding.imgCard.layoutParams = params

        var cardName: String

        if(Constants.CARD_TYPE_DEBIT==paymentCard?.cardType){
            cardName = Constants.TEXT_PRIMARY_CARD
        }else{
            if (null!=paymentCard?.nameUpdated) {
                if(paymentCard.nameUpdated!!){
                    cardName = paymentCard.cardName?: ""
                }else {
                    if(paymentCard.physical){
                        cardName = Constants.TEXT_SPARE_CARD_PHYSICAL
                    }else{
                        cardName = Constants.TEXT_SPARE_CARD_VIRTUAL
                    }
                }
            } else {
                if(paymentCard?.physical!!){
                    cardName = Constants.TEXT_SPARE_CARD_PHYSICAL
                }else{
                    cardName = Constants.TEXT_SPARE_CARD_VIRTUAL
                }

            }

        }
        itemYapCardBinding.tvCardName.text = cardName
        itemYapCardBinding.viewModel = YapCardItemViewModel(paymentCard,position, onItemClickListener)
        itemYapCardBinding.executePendingBindings()
    }
}