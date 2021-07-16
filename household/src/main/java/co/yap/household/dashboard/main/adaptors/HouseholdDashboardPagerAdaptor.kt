package co.yap.household.dashboard.main.adaptors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import co.yap.household.dashboard.home.fragments.HouseholdHomeFragment

class HouseholdDashboardPagerAdaptor(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var active = Fragment()

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> active = HouseholdHomeFragment()
            1 -> active = HouseholdHomeFragment()
            2 -> active = HouseholdHomeFragment()
            3 -> active = HouseholdHomeFragment()
        }
        return active
    }
}