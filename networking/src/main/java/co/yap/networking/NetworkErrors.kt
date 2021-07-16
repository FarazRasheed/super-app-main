package co.yap.networking

sealed class NetworkErrors {
    object NoInternet : NetworkErrors()
    object RequestTimedOut : NetworkErrors()
    object BadGateway : NetworkErrors()
    object NotFound : NetworkErrors()
    object Forbidden : NetworkErrors()
    class InternalServerError(val response: String?) : NetworkErrors()
    open class UnknownError : NetworkErrors()
}