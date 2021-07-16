package co.yap.modules.dashboard.more.help.adaptor

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemHelpSupportBinding
import co.yap.modules.dashboard.more.help.viewholder.HelpSupporttemViewHolder
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.helpers.Utils

class HelpSupportAdaptor(context: Context, private val list: MutableList<MoreOption>) :
    BaseBindingRecyclerAdapter<MoreOption, RecyclerView.ViewHolder>(list) {

    private var dimensions: IntArray = Utils.getCardDimensions(context, 43, 45)

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_help_support

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return HelpSupporttemViewHolder(binding as ItemHelpSupportBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is HelpSupporttemViewHolder) {
            holder.onBind(position, list[position], dimensions, onItemClickListener)
        }
    }
}
