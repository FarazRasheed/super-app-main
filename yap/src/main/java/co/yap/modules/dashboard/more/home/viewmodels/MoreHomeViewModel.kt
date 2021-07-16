package co.yap.modules.dashboard.more.home.viewmodels

import android.app.Application
import androidx.core.content.ContextCompat
import co.yap.R
import co.yap.modules.dashboard.more.home.interfaces.IMoreHome
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.modules.dashboard.more.home.states.MoreState
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.managers.SessionManager
import com.leanplum.Leanplum

class MoreHomeViewModel(application: Application) :
    MoreBaseViewModel<IMoreHome.State>(application), IMoreHome.ViewModel {

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: MoreState = MoreState()


    override fun onResume() {
        super.onResume()
        setPicture()
    }

    private fun setPicture() {
        SessionManager.user?.currentCustomer?.getPicture()?.let {
            state.image.set(it)
        }
        SessionManager.user?.currentCustomer?.getFullName()?.let {
            state.initials.set(Utils.shortName(it))
        }
    }

    override fun handlePressOnYAPforYou(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getMoreOptions(): MutableList<MoreOption> {
        val list = mutableListOf<MoreOption>()
        list.add(
            MoreOption(
                Constants.MORE_NOTIFICATION,
                "Notifications",
                R.drawable.ic_notification_more,
                ContextCompat.getColor(context, R.color.colorSecondaryOrange),
                false,
                Leanplum.getInbox().unreadCount()
            )
        )
        //colorSecondaryGreen
        list.add(
            MoreOption(
                Constants.MORE_LOCATE_ATM,
                "Locate ATM and CDM",
                R.drawable.ic_home_more,
                ContextCompat.getColor(context, R.color.colorSecondaryGreen),
                false,
                0
            )
        )
        list.add(
            MoreOption(
                Constants.MORE_INVITE_FRIEND,
                "Invite a friend",
                R.drawable.ic_gift,
                ContextCompat.getColor(context, R.color.colorPrimaryAlt),
                false,
                0
            )
        )
        list.add(
            MoreOption(
                Constants.MORE_HELP_SUPPORT,
                "Help and support",
                R.drawable.ic_support,
                ContextCompat.getColor(context, R.color.colorSecondaryBlue),
                false,
                0
            )
        )
        return list
    }
}