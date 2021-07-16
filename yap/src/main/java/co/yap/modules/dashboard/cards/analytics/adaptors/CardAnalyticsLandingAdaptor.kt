package co.yap.modules.dashboard.cards.analytics.adaptors

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.yap.modules.dashboard.cards.analytics.fragments.CategoryAnalyticsFragment
import co.yap.modules.dashboard.cards.analytics.fragments.MerchantAnalyticsFragment


const val CATEGORY_ANALYTICS = 0
const val MERCHANT_ANALYTICS = 1

class CardAnalyticsLandingAdaptor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        CATEGORY_ANALYTICS to { CategoryAnalyticsFragment() },
        MERCHANT_ANALYTICS to { MerchantAnalyticsFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}