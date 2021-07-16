package co.yap.modules.dashboard.more.notification.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.notification.activities.NotificationsActivity
import co.yap.modules.dashboard.more.notification.interfaces.INotificationDetail
import co.yap.modules.dashboard.more.notification.viewmodels.NotificationDetailViewModel
import co.yap.translation.Translator
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener
import com.leanplum.Leanplum

class NotificationDetailFragment : NotificationsBaseFragment<INotificationDetail.ViewModel>() {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_notification_detail


    override val viewModel: NotificationDetailViewModel
        get() = ViewModelProviders.of(this).get(NotificationDetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, clickObserver)
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.tvDeleteNotification -> {
                deleteAlertDialog()
            }
        }
    }

    private fun deleteAlertDialog() {
        context?.let { it ->
            Utils.confirmationDialog(it,
                Translator.getString(
                    it,
                    R.string.screen_notification_listing_display_text_delete_alert_title
                ),
                Translator.getString(
                    it,
                    R.string.screen_notification_listing_display_text_delete_message
                ), Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_yes
                ), Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_no
                ),
                object : OnItemClickListener {
                    override fun onItemClick(view: View, data: Any, pos: Int) {
                        if (data is Boolean) {
                            if (data) {
                                deleteNotification()
                            }
                        }
                    }
                })
        }
    }
    private fun deleteNotification() {
        val notification = viewModel.parentViewModel?.notification
        notification?.let {
            Leanplum.getInbox().messageForId(it.id).remove()
            (activity as? NotificationsActivity)?.onBackPressed()
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }
}