package co.yap.networking.transactions.responsedtos.achievement

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AchievementTask(
    @SerializedName("title")
    val title: String,
    @SerializedName("completion")
    val completion: Boolean
) : Parcelable