package co.yap.modules.dashboard.home.interfaces

import co.yap.modules.dashboard.home.models.HomeNotification

interface NotificationItemClickListener {
    fun onClick(notification: HomeNotification, position: Int)
    fun onCloseClick(notification: HomeNotification, position: Int) {}
}