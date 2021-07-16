package com.yap.yapandroid.modules.startup.adapters

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import co.yap.databinding.ContentOnboardingWelcomeBinding
import co.yap.modules.onboarding.models.WelcomeContent
import co.yap.yapcore.interfaces.IBindable

public class WelcomePagerAdapter(
    private val context: Context,
    private val contents: ArrayList<WelcomeContent>,
    private val layout: Int
) : PagerAdapter() {
    val viewsContainer: SparseArray<View> = SparseArray()

    var tvTitle: TextView? = null
    var tvDescription: TextView? = null
    var ivPoster: ImageView? = null
    var containerView: View? = null
    var check: Boolean = true

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val content = contents[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding = DataBindingUtil.inflate<ContentOnboardingWelcomeBinding>(
            inflater,
            layout,
            container,
            false
        )

        viewBinding.setVariable((content as IBindable).bindingVariable, content)

        tvTitle = viewBinding.tvTitle
        tvDescription = viewBinding.tvDescription
        ivPoster = viewBinding.ivPoster

        container.addView(viewBinding.root)
        containerView = viewBinding.root

        viewsContainer.put(position, viewBinding.root)

        return viewBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        viewsContainer.remove(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return contents[position].title
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === (`object` as View)

    override fun getCount(): Int = contents.size

}