package co.yap.modules.dashboard.home.status

import android.view.View
import co.yap.yapcore.interfaces.OnItemClickListener

class NotificationStatusItemViewModel (
    val statusDataModel: StatusDataModel,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, statusDataModel, position)
    }

}
