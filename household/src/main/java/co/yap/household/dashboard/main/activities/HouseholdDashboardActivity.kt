package co.yap.household.dashboard.main.activities

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import co.yap.household.R
import co.yap.household.dashboard.main.adaptors.HouseholdDashboardPagerAdaptor
import co.yap.household.dashboard.main.interfaces.IHouseholdDashboard
import co.yap.household.dashboard.main.viewmodels.HouseholdDashboardViewModel
import co.yap.household.databinding.ActivityHouseholdDashboardBinding
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.helpers.extentions.navViewWidth
import kotlinx.android.synthetic.main.activity_household_dashboard.*

class HouseholdDashboardActivity : BaseBindingActivity<IHouseholdDashboard.ViewModel>(),
    IFragmentHolder,
    IHouseholdDashboard.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_household_dashboard
    override val viewModel: IHouseholdDashboard.ViewModel
        get() = ViewModelProviders.of(this).get(HouseholdDashboardViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewBinding().drawerNav.navViewWidth(85)
        setupPager()
    }

    private fun setupPager() {
        val adapter = HouseholdDashboardPagerAdaptor(
            supportFragmentManager
        )
        getViewBinding().viewPager.adapter = adapter
        with(getViewBinding().viewPager) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }
        getViewBinding().viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                enableDrawerSwipe(position == 0)
            }
        })
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    override fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) closeDrawer()
        else openDrawer()
    }

    override fun enableDrawerSwipe(enable: Boolean) {
        if (enable) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        else drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun getViewBinding(): ActivityHouseholdDashboardBinding {
        return (viewDataBinding as ActivityHouseholdDashboardBinding)
    }
}