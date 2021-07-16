package co.yap.modules.dashboard.more.home.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.home.interfaces.IInviteFriend
import co.yap.modules.dashboard.more.home.states.InviteFriendState
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class InviteFriendViewModel(application: Application) :
    BaseViewModel<IInviteFriend.State>(application = application), IInviteFriend.ViewModel {
    override val state: InviteFriendState = InviteFriendState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        setUpStrings()
    }

    override fun handlePressOnButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun setUpStrings() {
        state.toolbarTitle = getString(Strings.screen_invite_friend_display_text_title)
        state.inviteTitle = getString(Strings.screen_invite_friend_display_text_reward)
        state.inviteDescription =
            getString(Strings.screen_invite_friend_display_text_referal_reward)
        state.referralLinkTextHeading =
            getString(Strings.screen_invite_friend_display_text_referal_code)
        state.buttonTitle = getString(Strings.screen_invite_friend_button_share)

    }

}