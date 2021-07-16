package co.yap.modules.dashboard.cards.addpaymentcard.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class BenefitsModel(
    var benfitTitle: String,
    var benfitDetail: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(benfitTitle)
        parcel.writeString(benfitDetail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BenefitsModel> {
        override fun createFromParcel(parcel: Parcel): BenefitsModel {
            return BenefitsModel(parcel)
        }

        override fun newArray(size: Int): Array<BenefitsModel?> {
            return arrayOfNulls(size)
        }
    }
}