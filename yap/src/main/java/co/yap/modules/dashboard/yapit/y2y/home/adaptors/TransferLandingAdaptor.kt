package co.yap.modules.dashboard.yapit.y2y.home.adaptors

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.yap.modules.dashboard.yapit.y2y.home.phonecontacts.PhoneContactFragment
import co.yap.modules.dashboard.yapit.y2y.home.yapcontacts.YapContactsFragment

const val YAP_CONTACTS = 0
const val PHONE_CONTACTS = 1

class TransferLandingAdaptor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        YAP_CONTACTS to { YapContactsFragment() },
        PHONE_CONTACTS to { PhoneContactFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}