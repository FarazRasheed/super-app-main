package co.yap.modules.otp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpDataModel(
    var otpAction: String? = "",
    var mobileNumber: String? = "",
    var username: String? = "",
    var emailOtp: Boolean? = false,
    var amount: String? = "",
    var logoData: LogoData? = null,
    var toolBarData: OtpToolBarData? = OtpToolBarData()
) : Parcelable