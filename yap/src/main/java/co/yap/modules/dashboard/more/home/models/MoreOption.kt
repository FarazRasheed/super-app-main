package co.yap.modules.dashboard.more.home.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class MoreOption(
    val id: Int,
    var name: String,
    val image: Int,
    val bgColor: Int,
    var hasBadge: Boolean,
    var badgeCount:Int
) : Parcelable