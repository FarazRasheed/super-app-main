package co.yap.widgets.skeletonlayout.mask

import android.view.View
import co.yap.widgets.skeletonlayout.SkeletonConfig

internal object SkeletonMaskFactory {

    fun createMask(
        view: View,
        config: SkeletonConfig
    ): SkeletonMask {
        return when (config.showShimmer) {
            true -> SkeletonMaskShimmer(view, config.maskColor, config.shimmerColor, config.shimmerDurationInMillis, config.shimmerDirection, config.shimmerAngle)
            false -> SkeletonMaskSolid(view, config.maskColor)
        }
    }
}