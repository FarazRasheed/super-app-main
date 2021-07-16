package co.yap.modules.otp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LogoData(
    var imageUrl: String? = "",
    var position: Int? = 0,
    var flagVisibility: Boolean? = false,
    var beneficiaryCountry: String? = ""
) : Parcelable {
}