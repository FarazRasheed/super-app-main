package co.yap.modules.dashboard.more.home.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.more.home.interfaces.IInviteFriend
import co.yap.yapcore.BaseState

class InviteFriendState : BaseState(), IInviteFriend.State {
    @get:Bindable
    override var inviteTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inviteTitle)
        }

    @get:Bindable
    override var inviteDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inviteDescription)
        }

    @get:Bindable
    override var referralLinkTextHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.referralLinkTextHeading)
        }

    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }
}