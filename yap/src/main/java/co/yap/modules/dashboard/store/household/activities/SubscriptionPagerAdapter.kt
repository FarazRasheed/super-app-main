package co.yap.modules.dashboard.store.household.activities

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import co.yap.databinding.ContentSubscriptionSelectionPagerBinding
import co.yap.modules.onboarding.models.WelcomeContent
import co.yap.yapcore.interfaces.IBindable


public class SubscriptionPagerAdapter(
    private val context: Context,
    private val contents: ArrayList<WelcomeContent>,
    private val layout: Int
) : PagerAdapter() {
    val viewsContainer: SparseArray<View> = SparseArray()

    var tvDescription: TextView? = null
    var ivPoster: ImageView? = null
    var containerView: View? = null
    var check: Boolean = true

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val content = contents[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewBinding = DataBindingUtil.inflate<ContentSubscriptionSelectionPagerBinding>(
            inflater,
            layout,
            container,
            false
        )

        viewBinding.setVariable((content as IBindable).bindingVariable, content)

        tvDescription = viewBinding.tvDescription
        ivPoster = viewBinding.ivPoster


        //viewBinding.tvDescription.visibility=GONE
        //viewBinding.ivPoster.visibility=GONE

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