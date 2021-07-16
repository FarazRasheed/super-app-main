package co.yap.networking.transactions.responsedtos.achievement

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Achievement(
    @SerializedName("colorCode")
    val colorCode: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("percentage")
    var percentage: Double?,
    @SerializedName("features")
    val features: List<AchievementTask>?,
    var icon: Int = -1
) : Parcelable