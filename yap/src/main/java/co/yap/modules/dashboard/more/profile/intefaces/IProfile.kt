package co.yap.modules.dashboard.more.profile.intefaces

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.app.YAPApplication
import co.yap.networking.authentication.AuthRepository
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import java.io.File

interface IProfile {
    interface State : IBase.State {
        var fullName: String
        var profilePictureUrl: String
        var nameInitialsVisibility: Int
        var imageUri: Uri
        var buildVersionDetail: String?
        var isShowErrorIcon: ObservableField<Boolean>

    }

    interface ViewModel : IBase.ViewModel<State> {
        val authRepository: AuthRepository
        var clickEvent: SingleClickEvent
        var PROFILE_PICTURE_UPLOADED: Int
        var EVENT_LOGOUT_SUCCESS: Int
        fun handlePressOnViewClick(id: Int)
        fun requestProfileDocumentsInformation()
        fun requestUploadProfilePicture(actualFile: File)
        fun logout()
        fun requestRemoveProfilePicture(apiRes: (Boolean)-> Unit)

    }

    interface View : IBase.View<ViewModel> {
        val versionName: String? get() = "Version " + YAPApplication.configManager?.versionName + " (" + YAPApplication.configManager?.versionCode + ")"
    }
}