package co.yap.widgets.skeletonlayout.mask

internal interface SkeletonMaskable {
    fun invalidate() = Unit
    fun start() = Unit
    fun stop() = Unit
}