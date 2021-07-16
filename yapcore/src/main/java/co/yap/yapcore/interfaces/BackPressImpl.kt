package co.yap.yapcore.interfaces

import androidx.fragment.app.Fragment


class BackPressImpl(private val parentFragment: Fragment?) : OnBackPressedListener {

    override fun onBackPressed(): Boolean =
        run {
            if (parentFragment == null) return false
            if (parentFragment.childFragmentManager.fragments.isNotEmpty())
                (parentFragment.childFragmentManager.fragments[0] as? OnBackPressedListener)?.onBackPressed()!!
            else false
        }
}