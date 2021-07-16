package co.yap.modules.yapnotification.interfaces

import co.yap.modules.yapnotification.models.Notification


interface NotificationItemClickListener {
    fun onClick(notification: Notification)
}