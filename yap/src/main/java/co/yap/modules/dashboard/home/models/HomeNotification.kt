package co.yap.modules.dashboard.home.models

import co.yap.yapcore.enums.NotificationAction

data class HomeNotification(
    val id: String="",
    val title: String = "",
    val description: String,
    val action: NotificationAction
)