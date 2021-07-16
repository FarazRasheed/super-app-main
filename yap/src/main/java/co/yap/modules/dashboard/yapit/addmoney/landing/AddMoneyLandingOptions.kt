package co.yap.modules.dashboard.yapit.addmoney.landing

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class AddMoneyLandingOptions(
    val id: Int,
    var name: String,
    val image: Int
) : Parcelable