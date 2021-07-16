package co.yap.widgets.skeletonlayout.viewpager2

import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import co.yap.widgets.skeletonlayout.Skeleton
import co.yap.widgets.skeletonlayout.SkeletonConfig
import co.yap.widgets.skeletonlayout.SkeletonStyle
import co.yap.widgets.skeletonlayout.recyclerview.SkeletonRecyclerViewAdapter

internal class SkeletonViewPager2(
    private val viewPager: ViewPager2,
    @LayoutRes layoutResId: Int,
    itemCount: Int,
    config: SkeletonConfig
) : Skeleton, SkeletonStyle by config {

    private val originalAdapter = viewPager.adapter
    private var skeletonAdapter = SkeletonRecyclerViewAdapter(layoutResId, itemCount, config)

    init {
        config.addValueObserver { skeletonAdapter.notifyDataSetChanged() }
    }

    override fun showOriginal() {
        viewPager.adapter = originalAdapter
    }

    override fun showSkeleton() {
        viewPager.adapter = skeletonAdapter
    }

    override fun isSkeleton(): Boolean {
        return viewPager.adapter == skeletonAdapter
    }
}