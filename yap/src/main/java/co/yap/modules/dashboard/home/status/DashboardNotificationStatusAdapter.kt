package co.yap.modules.dashboard.home.status

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemDashboardNotificationStatusBinding
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener

class DashboardNotificationStatusAdapter(
    context: Context,
    private val list: MutableList<StatusDataModel>
) :
    BaseBindingRecyclerAdapter<StatusDataModel, RecyclerView.ViewHolder>(list) {
    private var dimensions: IntArray = Utils.getCardDimensions(context, 43, 45)

    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.item_dashboard_notification_status

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return DashboardNotificationStatusViewHolder(binding as ItemDashboardNotificationStatusBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is DashboardNotificationStatusViewHolder) {
            holder.onBind(position, list[position], dimensions, onItemClickListener)
        }
    }

    class DashboardNotificationStatusViewHolder(private val itemDashboardNotificationStatusBinding: ItemDashboardNotificationStatusBinding) :
        RecyclerView.ViewHolder(itemDashboardNotificationStatusBinding.root) {

        fun onBind(
            position: Int,
            statusDataModel: StatusDataModel,
            dimensions: IntArray,
            onItemClickListener: OnItemClickListener?
        ) {
            itemDashboardNotificationStatusBinding.viewModel =
                NotificationStatusItemViewModel(statusDataModel, position, onItemClickListener)
            itemDashboardNotificationStatusBinding.executePendingBindings()

        }

        init {
            itemDashboardNotificationStatusBinding.timeline.initLine(1)
        }
    }
}
