package co.yap.modules.dashboard.more.help.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentHelpSupportBinding
import co.yap.modules.dashboard.main.activities.YapDashboardActivity
import co.yap.modules.dashboard.more.help.adaptor.HelpSupportAdaptor
import co.yap.modules.dashboard.more.help.interfaces.IHelpSupport
import co.yap.modules.dashboard.more.help.viewmodels.HelpSupportViewModel
import co.yap.modules.dashboard.more.main.fragments.MoreBaseFragment
import co.yap.modules.webview.WebViewFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.*
import co.yap.yapcore.managers.SessionManager
import com.liveperson.infra.CampaignInfo
import com.liveperson.infra.ConversationViewParams
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.LPConversationsHistoryStateToDisplay
import com.liveperson.messaging.sdk.api.LivePerson
import com.liveperson.messaging.sdk.api.model.ConsumerProfile
import co.yap.yapcore.managers.ChatManager

class HelpSupportFragment : MoreBaseFragment<IHelpSupport.ViewModel>(), IHelpSupport.View {

    lateinit var adapter: HelpSupportAdaptor

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_help_support

    private val appInstallId = SessionManager.user?.uuid

    override val viewModel: IHelpSupport.ViewModel
        get() = ViewModelProviders.of(this).get(HelpSupportViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHelpDeskPhone()
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, observer)
        viewModel.urlUpdated.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                openFaqsPage(it)
            } else {
                showToast("Invalid url.")
            }
        })
    }

    private val observer = Observer<Int> {
        when (it) {
            R.id.lLyFaqs -> {
                viewModel.getFaqsUrl()
            }
            R.id.lyChat -> {
                requireActivity().chatSetup()
//                activity?.let { activity ->
//                    ChatManager.config(activity)
//                }
            }
            R.id.lyLiveWhatsApp -> {
                if (requireContext().isWhatsAppInstalled()) {
                    requireContext().openWhatsApp()
                } else {
                    requireContext().openUrl("https://web.whatsapp.com/")
                }
            }
            R.id.lyCall -> {
                requireContext().makeCall(viewModel.state.contactPhone.get())
            }

        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                activity?.finish()
            }
        }
    }

    @Nullable
    fun getCampaignInfo(): CampaignInfo? {
        return CampaignInfo(
            "campaignId".toLong(), "engagementId".toLong(),
            "interactionContextId", "sessionId",
            "visitorId"
        )
    }

    private fun openFaqsPage(url: String) {
        startFragment(
            fragmentName = WebViewFragment::class.java.name,
            bundle = bundleOf(
                Constants.PAGE_URL to url
            ), toolBarTitle = viewModel.state.toolbarTitle ?: "", showToolBar = false
        )
    }

    override fun onResume() {
        super.onResume()
        if (activity is YapDashboardActivity)
            (activity as YapDashboardActivity).showHideBottomBar(false)
    }

    override fun onStop() {
        super.onStop()
        if (activity is YapDashboardActivity)
            (activity as YapDashboardActivity).showHideBottomBar(true)
    }

    override fun getBinding(): FragmentHelpSupportBinding {
        return viewDataBinding as FragmentHelpSupportBinding
    }
}