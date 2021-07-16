package co.yap.modules.dashboard.more.notification.adaptor

import androidx.databinding.ViewDataBinding
import co.yap.R
import co.yap.databinding.ItemNotificationBinding
import co.yap.modules.dashboard.more.notification.viewholder.NotificationViewHolder
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.BaseBindingRecyclerAdapter

class NotificationsAdaptor(private val list: MutableList<Notification>) :
    BaseBindingRecyclerAdapter<Notification, NotificationViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_notification

    override fun onCreateViewHolder(binding: ViewDataBinding): NotificationViewHolder {
        return NotificationViewHolder(binding as ItemNotificationBinding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position])
    }

}