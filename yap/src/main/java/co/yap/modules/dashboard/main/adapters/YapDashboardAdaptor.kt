package co.yap.modules.dashboard.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import co.yap.modules.dashboard.cards.home.fragments.YapCardsFragment
import co.yap.modules.dashboard.home.fragments.YapHomeFragment
import co.yap.modules.dashboard.more.home.fragments.YapMoreFragment
import co.yap.modules.dashboard.store.fragments.YapStoreFragment

class YapDashboardAdaptor(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var active = Fragment()

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> active = YapHomeFragment()
            1 -> active = YapStoreFragment()
            2 -> active = YapCardsFragment()
            3 -> active = YapMoreFragment()
        }
        return active
    }
}