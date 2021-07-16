package co.yap.modules.dashboard.more.notification.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentNotificationsHomeBinding
import co.yap.modules.dashboard.more.notification.interfaces.INotificationHome
import co.yap.modules.dashboard.more.notification.viewmodels.NotificationsHomeViewModel
import co.yap.translation.Translator
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener
import com.leanplum.Leanplum
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import java.util.*

class NotificationHomeFragment : NotificationsBaseFragment<INotificationHome.ViewModel>() {
    private lateinit var onTouchListener: RecyclerTouchListener

    override val viewModel: NotificationsHomeViewModel
        get() = ViewModelProviders.of(this).get(NotificationsHomeViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_notifications_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.parentViewModel?.clickEvent?.observe(this, clickEventObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().recyclerNotification.adapter = viewModel.adapter
        initSwipeListener()
        getBinding().recyclerNotification.addOnItemTouchListener(onTouchListener)
    }

    private val clickEventObserver = Observer<Int> {
        when (it) {
            R.id.tbBtnSettings -> {
                val action =
                    NotificationHomeFragmentDirections.actionNotificationHomeFragmentToNotificationSettingsFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initSwipeListener() {
        onTouchListener =
            RecyclerTouchListener(requireActivity(), getBinding().recyclerNotification)
                .setClickable(
                    object : RecyclerTouchListener.OnRowClickListener {
                        override fun onRowClicked(position: Int) {
                            openDetailScreen(position)
                        }
                        override fun onIndependentViewClicked(
                            independentViewID: Int,
                            position: Int
                        ) {
                        }
                    }).setSwipeOptionViews(R.id.btnDelete)
                .setSwipeable(
                    R.id.foregroundContainer, R.id.swipe
                )
                { viewID, position ->
                    when (viewID) {
                        R.id.btnDelete -> {
                            deleteNotification(position)
//                            deleteAlertDialog(position)
                        }
                    }
                }
    }


    private fun deleteAlertDialog(position: Int) {
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
                                deleteNotification(position)
                            }
                        }
                    }
                })
        }
    }

    private fun openDetailScreen(position: Int) {
        viewModel.parentViewModel?.notification = viewModel.adapter.getDataForPosition(position)

        viewModel.parentViewModel?.notification?.also {
            it.date = DateUtils.dateToString(
                DateUtils.stringToDateLeanPlum(it.date ?: "") ?: Date(),
                DateUtils.LEAN_PLUM_FORMAT
            )
        }

        Leanplum.getInbox().messageForId(viewModel.parentViewModel?.notification?.id).read()

        val action =
            NotificationHomeFragmentDirections.actionNotificationHomeFragmentToNotificationDetailFragment()
        findNavController().navigate(action)
    }

    private fun deleteNotification(position: Int) {
        val notification = viewModel.adapter.getDataForPosition(position)
        Leanplum.getInbox().messageForId(notification.id).remove()
        viewModel.adapter.removeItemAt(position)
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadNotifications()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getBinding().recyclerNotification.removeOnItemTouchListener(onTouchListener)
    }

    fun getBinding(): FragmentNotificationsHomeBinding {
        return viewDataBinding as FragmentNotificationsHomeBinding
    }
}