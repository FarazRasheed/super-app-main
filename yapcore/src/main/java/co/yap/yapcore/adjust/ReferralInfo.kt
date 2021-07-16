package co.yap.yapcore.adjust

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ReferralInfo(val id:String,val date:String):Parcelable