package co.yap.modules.dashboard.cards.analytics.adaptors.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemAnalyticsBinding
import co.yap.modules.dashboard.cards.analytics.adaptors.CategoryAnalyticsAdaptor
import co.yap.modules.dashboard.cards.analytics.viewmodels.AnalyticsItemViewModel
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.yapcore.helpers.extentions.getColors
import co.yap.yapcore.interfaces.OnItemClickListener

class CategoryAnalyticsItemViewHolder(private val itemAnalyticsBinding: ItemAnalyticsBinding) :
    RecyclerView.ViewHolder(itemAnalyticsBinding.root) {
    fun onBind(
        adapter: CategoryAnalyticsAdaptor?,
        analyticsItem: TxnAnalytic?,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        if (adapter?.checkedPosition != -1) {

            itemView.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        if (adapter?.checkedPosition == position) R.color.itemBackground else R.color.white
                    )
                )
                itemAnalyticsBinding.tvName.setTextColor(
                    if (adapter?.checkedPosition == position) context.getColors(R.color.colorMidnightExpress)
                    else context.getColors(R.color.colorMidnightExpress)
                )
                isSelected = adapter?.checkedPosition == position
            }

        }

        itemAnalyticsBinding.viewModel =
            AnalyticsItemViewModel(analyticsItem, position, onItemClickListener)
        itemAnalyticsBinding.executePendingBindings()
    }
}
