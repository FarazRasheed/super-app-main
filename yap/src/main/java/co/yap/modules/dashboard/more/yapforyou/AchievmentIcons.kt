package co.yap.modules.dashboard.more.yapforyou

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AchievmentIcons(var roundBadgeIcon: Int? = null, var mainBadgeIcon: Int? = null) :
    Parcelable