package co.yap.widgets.spinneradapter.searchable

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes

/**
 * Created by mudassar on 6/4/20.
 */
class SelectedView : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SelectedView
        if (mPosition != that.mPosition) return false
        if (mId != that.mId) return false
        return if (mView != null) mView == that.mView else that.mView == null
    }

     var mView: View? = null
     var mPosition = 0
    @IdRes
     var mId: Long = 0

    constructor(
        view: View?, position: Int,
        @IdRes id: Long
    ) {
        mView = view
        mPosition = position
        mId = id

    }
    constructor(
        position: Int,
        @IdRes id: Long
    ) {
        mPosition = position
        mId = id

    }

    override fun hashCode(): Int {
        var result = if (mView != null) mView.hashCode() else 0
        result = 31 * result + mPosition
        result = 31 * result + (mId xor (mId ushr 32)).toInt()
        return result
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(mPosition)
        writeLong(mId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SelectedView> = object : Parcelable.Creator<SelectedView> {
            override fun createFromParcel(source: Parcel): SelectedView = SelectedView(source)
            override fun newArray(size: Int): Array<SelectedView?> = arrayOfNulls(size)
        }
    }
}