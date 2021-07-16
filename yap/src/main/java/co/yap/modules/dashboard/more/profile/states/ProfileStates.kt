package co.yap.modules.dashboard.more.profile.states

import android.net.Uri
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import co.yap.modules.dashboard.more.profile.intefaces.IProfile
import co.yap.yapcore.BaseState

class ProfileStates : BaseState(), IProfile.State {

    @get:Bindable
    override var nameInitialsVisibility: Int = VISIBLE
        set(value) {
            field = value
            notifyPropertyChanged(BR.nameInitialsVisibility)

        }

    @get:Bindable
    override var fullName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fullName)

        }

    @get:Bindable
    override var imageUri: Uri = Uri.EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.imageUri)
        }

    @get:Bindable
    override var buildVersionDetail: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buildVersionDetail)
        }

    @get:Bindable
    override var profilePictureUrl: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.profilePictureUrl)
            if (!field.isNullOrEmpty()) {
                nameInitialsVisibility = VISIBLE
                notifyPropertyChanged(BR.nameInitialsVisibility)

            } else {
                nameInitialsVisibility = GONE
                notifyPropertyChanged(BR.nameInitialsVisibility)

            }
        }


    override var isShowErrorIcon: ObservableField<Boolean> = ObservableField(false)
}