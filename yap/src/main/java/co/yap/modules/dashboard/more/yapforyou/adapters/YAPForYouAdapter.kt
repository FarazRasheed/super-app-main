package co.yap.modules.dashboard.more.yapforyou.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemYapForYouBinding
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.yapcore.BaseBindingRecyclerAdapter

class YAPForYouAdapter(private val list: MutableList<Achievement>) :
    BaseBindingRecyclerAdapter<Achievement, RecyclerView.ViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_yap_for_you

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return YAPForYouItemViewHolder(binding as ItemYapForYouBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is YAPForYouItemViewHolder) {
            holder.onBind(position, list[position], onItemClickListener)
        }
    }
}
