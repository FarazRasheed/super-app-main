package co.yap.networking.interfaces

internal interface TokenValidator {
    var tokenRefreshInProgress: Boolean
    fun invalidate()
}