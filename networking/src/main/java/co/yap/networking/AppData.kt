package co.yap.networking

data class AppData(
    var flavor: String = "",
    var build_type: String = "",
    var baseUrl: String = "",
    var sslPin1: String?,
    var sslPin2: String?,
    var sslPin3: String?,
    var sslHost: String?
) {
    fun isReleaseMode(): Boolean = (build_type == "release")
    //fun isStgOrLiveMode(): Boolean = (flavor == "stg") || (flavor == "live")
}