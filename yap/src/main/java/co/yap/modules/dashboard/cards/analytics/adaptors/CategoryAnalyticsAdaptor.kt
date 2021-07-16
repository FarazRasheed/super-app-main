package co.yap.modules.dashboard.cards.analytics.adaptors

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemAnalyticsBinding
import co.yap.modules.dashboard.cards.analytics.adaptors.viewholders.CategoryAnalyticsItemViewHolder
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.yapcore.BaseBindingRecyclerAdapter

class CategoryAnalyticsAdaptor(private val list: MutableList<TxnAnalytic>) :
    BaseBindingRecyclerAdapter<TxnAnalytic, RecyclerView.ViewHolder>(list) {

    var checkedPosition: Int = -1
    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_analytics

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return CategoryAnalyticsItemViewHolder(binding as ItemAnalyticsBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is CategoryAnalyticsItemViewHolder) {
            holder.onBind(this,list[position], position, onItemClickListener)
        }
    }
}
