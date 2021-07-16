package co.yap.modules.dashboard.more.home.adaptor

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemYapMoreBinding
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.modules.dashboard.more.home.viewholder.YapMoreItemViewHolder
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.helpers.Utils

class YapMoreAdaptor(context: Context, private val list: MutableList<MoreOption>) :
    BaseBindingRecyclerAdapter<MoreOption, RecyclerView.ViewHolder>(list) {

    private var dimensions: IntArray = Utils.getCardDimensions(context, 43, 45)

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_yap_more

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return YapMoreItemViewHolder (binding as ItemYapMoreBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is YapMoreItemViewHolder) {
            holder.onBind(position, list[position], dimensions, onItemClickListener)
        }
    }
}
