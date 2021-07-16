package co.yap.modules.dashboard.more.changepasscode.models

data class PassCodeData(
    var token: String = "",
    var mobileNo: String = "",
    var username: String = "",
    var oldPassCode: String? = "",
    var newPassCode: String? = ""
)