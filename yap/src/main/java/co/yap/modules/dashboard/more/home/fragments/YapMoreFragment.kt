package co.yap.modules.dashboard.more.home.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentMoreHomeBinding
import co.yap.modules.dashboard.main.fragments.YapDashboardChildFragment
import co.yap.modules.dashboard.more.bankdetails.activities.BankDetailActivity
import co.yap.modules.dashboard.more.cdm.CdmMapFragment
import co.yap.modules.dashboard.more.home.adaptor.YapMoreAdaptor
import co.yap.modules.dashboard.more.home.interfaces.IMoreHome
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.modules.dashboard.more.home.viewmodels.MoreHomeViewModel
import co.yap.modules.dashboard.more.main.activities.MoreActivity
import co.yap.modules.dashboard.more.notification.activities.NotificationsActivity
import co.yap.modules.dashboard.more.yapforyou.activities.YAPForYouActivity
import co.yap.modules.others.fragmentpresenter.activities.FragmentPresenterActivity
import co.yap.widgets.SpaceGridItemDecoration
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.dimen
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.helpers.extentions.maskIbanNumber
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.leanplum.Leanplum


class YapMoreFragment : YapDashboardChildFragment<IMoreHome.ViewModel>(), IMoreHome.View {

    lateinit var adapter: YapMoreAdaptor
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_more_home

    override val viewModel: IMoreHome.ViewModel
        get() = ViewModelProviders.of(this).get(MoreHomeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        setupRecycleView()
    }

    override fun onResume() {
        super.onResume()
        initComponents()
        updateNotificationCounter()
    }

    private fun updateNotificationCounter() {
        if (::adapter.isInitialized) {
            if (!adapter.getDataList().isNullOrEmpty()) {
                val item = adapter.getDataForPosition(0)
                item.hasBadge = false //Leanplum.getInbox().unreadCount() > 0
                item.badgeCount = Leanplum.getInbox().unreadCount()
                adapter.setItemAt(0, item)
            }
        }
    }

    private fun initComponents() {
        getBinding().tvName.text =
            SessionManager.user?.currentCustomer?.getFullName()

        val ibanSpan = SpannableString("IBAN ${SessionManager.user?.iban?.maskIbanNumber()}")
        getBinding().tvIban.text = Utils.setSpan(
            0,
            4,
            ibanSpan,
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        )

        SessionManager.user?.bank?.swiftCode?.let {
            val bicSpan = SpannableString("BIC $it")
            getBinding().tvBic.text = Utils.setSpan(
                0,
                3,
                bicSpan,
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            )
        }

    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun setupRecycleView() {
        adapter = YapMoreAdaptor(requireContext(), viewModel.getMoreOptions())
        getBinding().recyclerOptions.adapter = adapter

        getBinding().recyclerOptions.addItemDecoration(
            SpaceGridItemDecoration(
                dimen(R.dimen.margin_normal_large) ?: 16, 2, true
            )
        )
        adapter.allowFullItemClickListener = true
        adapter.setItemListener(listener)
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, observer)
    }

    override fun removeObservers() {
        viewModel.clickEvent.removeObservers(this)
    }

    private val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is MoreOption)
                viewModel.clickEvent.setValue(data.id)
        }
    }

    private fun openNotifications() {
        startActivity(Intent(requireContext(), NotificationsActivity::class.java))
    }

    private fun openMaps() {
        //for zoom level z=zoom
        val uri = Uri.parse("geo:3.4241,53.847?q=" + Uri.encode("Rakbank Atm"))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private val observer = Observer<Int> {
        when (it) {
            R.id.imgProfile -> {
                startActivity(MoreActivity.newIntent(requireContext()))
            }
            R.id.imgSettings -> {
                startActivity(MoreActivity.newIntent(requireContext()))
            }
            R.id.tvName -> {
                startActivity(MoreActivity.newIntent(requireContext()))
            }
            R.id.tvNameInitials -> {
                startActivity(MoreActivity.newIntent(requireContext()))
            }
            R.id.tvIban -> {
            }
            R.id.btnBankDetails -> {
                startActivity(BankDetailActivity.newIntent(requireContext()))
            }
            R.id.yapForYou -> {
                launchActivity<YAPForYouActivity>(type = FeatureSet.YAP_FOR_YOU)
            }
            Constants.MORE_NOTIFICATION -> {
                Utils.showComingSoon(requireContext())
            }
            Constants.MORE_LOCATE_ATM -> {
                startFragment(CdmMapFragment::class.java.name)
            }
            Constants.MORE_INVITE_FRIEND -> {
                startFragment(
                    InviteFriendFragment::class.java.name, false,
                    bundleOf()
                )
            }
            Constants.MORE_HELP_SUPPORT -> {
                startActivity(
                    FragmentPresenterActivity.getIntent(
                        requireContext(),
                        Constants.MODE_HELP_SUPPORT, null
                    )
                )
            }
        }
    }

    override fun getBinding(): FragmentMoreHomeBinding {
        return viewDataBinding as FragmentMoreHomeBinding
    }
}