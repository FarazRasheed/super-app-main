@file:JvmName("SkeletonLayoutUtils")

package co.yap.widgets.skeletonlayout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.yap.widgets.skeletonlayout.recyclerview.SkeletonRecyclerView
import co.yap.widgets.skeletonlayout.viewpager2.SkeletonViewPager2

private const val LIST_ITEM_COUNT_DEFAULT = 3
internal fun Any.tag(): String = javaClass.simpleName

@JvmOverloads
fun View.createSkeleton(
    config: SkeletonConfig = SkeletonConfig.default(context)
): Skeleton {
    // If this View already has a parent, we need to replace it with the SkeletonLayout
    val parent = (parent as? ViewGroup)
    val index = parent?.indexOfChild(this) ?: 0
    val params = layoutParams

    parent?.removeView(this)

    val skeleton = SkeletonLayout(this, config)

    if (params != null) {
        skeleton.layoutParams = params
    }
    parent?.addView(skeleton, index)

    return skeleton
}

@JvmOverloads
fun RecyclerView.applySkeleton(
    @LayoutRes listItemLayoutResId: Int,
    itemCount: Int = LIST_ITEM_COUNT_DEFAULT,
    config: SkeletonConfig = SkeletonConfig.default(context)
): Skeleton = SkeletonRecyclerView(this, listItemLayoutResId, itemCount, config)

@JvmOverloads
fun ViewPager2.applySkeleton(
    @LayoutRes listItemLayoutResId: Int,
    itemCount: Int = LIST_ITEM_COUNT_DEFAULT,
    config: SkeletonConfig = SkeletonConfig.default(context)
): Skeleton = SkeletonViewPager2(this, listItemLayoutResId, itemCount, config)

internal fun Context.refreshRateInSeconds(): Float {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    return windowManager?.defaultDisplay?.refreshRate ?: 60f
}

internal fun ViewGroup.views(): List<View> {
    return (0 until childCount).map { child -> getChildAt(child) }
}

internal fun View.isAttachedToWindowCompat(): Boolean {
    return ViewCompat.isAttachedToWindow(this)
}