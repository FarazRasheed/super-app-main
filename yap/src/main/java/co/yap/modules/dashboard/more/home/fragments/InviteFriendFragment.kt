package co.yap.modules.dashboard.more.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.home.interfaces.IInviteFriend
import co.yap.modules.dashboard.more.home.viewmodels.InviteFriendViewModel
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.helpers.Utils


class InviteFriendFragment : BaseBindingFragment<IInviteFriend.ViewModel>(), IInviteFriend.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_invite_friend

    override val viewModel: IInviteFriend.ViewModel
        get() = ViewModelProviders.of(this).get(InviteFriendViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
    }

    val clickEvent = Observer<Int> {
        when (it) {
            R.id.btnShare -> {
                shareInfo()
            }
        }
    }

    private fun shareInfo() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getBody())
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

    private fun getBody(): String {
        return Translator.getString(
            requireContext(),
            Strings.screen_invite_friend_display_text_share_url,
            Utils.getAdjustURL()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                activity?.finish()
            }
        }
    }
}