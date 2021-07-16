package co.yap.household.dashboard.home.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.household.R
import co.yap.household.dashboard.home.interfaces.IHouseholdHome
import co.yap.household.dashboard.home.viewmodels.HouseholdHomeViewModel
import co.yap.household.dashboard.main.fragments.HouseholdDashboardBaseFragment
import co.yap.household.databinding.FragmentHouseholdHomeBinding
import co.yap.modules.yapnotification.adaptors.YapNotificationAdapter
import co.yap.modules.yapnotification.interfaces.NotificationItemClickListener
import co.yap.modules.yapnotification.models.Notification
import co.yap.widgets.MultiStateView
import co.yap.yapcore.BR
import co.yap.yapcore.constants.Constants
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

class HouseholdHomeFragment : HouseholdDashboardBaseFragment<IHouseholdHome.ViewModel>(),
    IHouseholdHome.View, NotificationItemClickListener {

    private var mAdapter: YapNotificationAdapter? = null
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_household_home
    override val viewModel: IHouseholdHome.ViewModel
        get() = ViewModelProviders.of(this).get(HouseholdHomeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        initNotificationAdaptor()
    }

    private fun initNotificationAdaptor() {
        mAdapter = YapNotificationAdapter(
            ArrayList(),
            requireContext(),
            this
        )
    }

    override fun setObservers() {
        viewModel.viewState.observe(this, viewStateObserver)
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.notificationList.observe(this, Observer {
            setNotificationAdapter(it)
            setDiscreteScrollView()
        })
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.ivMenu -> {
                parentView?.toggleDrawer()
            }
            R.id.ivSearch -> {
            }
        }
    }

    private val viewStateObserver = Observer<Int> {
        when (it) {
            Constants.EVENT_LOADING -> {
                getViewBinding().multiStateView.viewState = MultiStateView.ViewState.LOADING
            }
            Constants.EVENT_EMPTY -> {
                getViewBinding().multiStateView.viewState = MultiStateView.ViewState.EMPTY
            }
            Constants.EVENT_CONTENT -> {
                getViewBinding().multiStateView.viewState = MultiStateView.ViewState.CONTENT
            }
        }

    }

    private fun setNotificationAdapter(notificationList: ArrayList<Notification>) {
        mAdapter = YapNotificationAdapter(
            notificationList,
            requireContext(),
            this
        )
        mAdapter?.notifyDataSetChanged()
    }

    private fun setDiscreteScrollView() {
        getViewBinding().rvNotificationList.setSlideOnFling(false)
        getViewBinding().rvNotificationList.setOverScrollEnabled(true)
        getViewBinding().rvNotificationList.adapter = mAdapter
        //rvNotificationList.addOnItemChangedListener(this)
        //rvNotificationList.addScrollStateChangeListener(this)
        getViewBinding().rvNotificationList.smoothScrollToPosition(0)
        getViewBinding().rvNotificationList.setItemTransitionTimeMillis(100)
        getViewBinding().rvNotificationList.setItemTransformer(
            ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewState.removeObserver(viewStateObserver)
        viewModel.clickEvent.removeObserver(clickObserver)
    }

    private fun getViewBinding(): FragmentHouseholdHomeBinding {
        return (viewDataBinding as FragmentHouseholdHomeBinding)
    }

    override fun onClick(notification: Notification) {

    }
}