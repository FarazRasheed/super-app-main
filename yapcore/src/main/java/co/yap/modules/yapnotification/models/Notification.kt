package co.yap.modules.yapnotification.models

class Notification(
    var title: String?,
    var description: String?,
    var type: String?,
    var action: String?,
    var imageUrl: String?,
    var sticky: String?,
    var date: String? = "",
    val isRead: Boolean? = false,
    var id: String? = ""
)
