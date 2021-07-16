package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.modules.dashboard.more.profile.intefaces.IProfile
import co.yap.modules.dashboard.more.profile.states.ProfileStates
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants.KEY_APP_UUID
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.EIDStatus
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.extentions.sizeInMb
import co.yap.yapcore.leanplum.KYCEvents
import co.yap.yapcore.leanplum.getFormattedDate
import co.yap.yapcore.leanplum.trackEvent
import co.yap.yapcore.leanplum.trackEventWithAttributes
import co.yap.yapcore.managers.SessionManager
import com.bumptech.glide.Glide
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileSettingsViewModel(application: Application) :
    MoreBaseViewModel<IProfile.State>(application), IProfile.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override var PROFILE_PICTURE_UPLOADED: Int = 100
    override var EVENT_LOGOUT_SUCCESS: Int = 101
    override val authRepository: AuthRepository = AuthRepository
    override val repository: CustomersRepository = CustomersRepository
    private val sharedPreferenceManager = SharedPreferenceManager(application)

    override val state: ProfileStates =
        ProfileStates()

    override var clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnViewClick(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onResume() {
        super.onResume()
        toggleAchievementsBadgeVisibility(parentViewModel!!.BadgeVisibility)
        setToolBarTitle(getString(Strings.common_button_settings))
    }

    override fun onCreate() {
        super.onCreate()
        requestProfileDocumentsInformation()
        SessionManager.user?.let {
            state.fullName = it.currentCustomer.getFullName()
            if (it.currentCustomer.getPicture() != null) {
                state.profilePictureUrl = it.currentCustomer.getPicture()!!
            } else {
                state.fullName = it.currentCustomer.getFullName()
                state.nameInitialsVisibility = GONE
            }
        }
    }

    override fun logout() {
        val deviceId: String? =
            sharedPreferenceManager.getValueString(KEY_APP_UUID)
        launch {
            state.loading = true
            when (val response = authRepository.logout(deviceId.toString())) {
                is RetroApiResponse.Success -> {
                    clickEvent.setValue(EVENT_LOGOUT_SUCCESS)
                    state.loading = true
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun requestUploadProfilePicture(actualFile: File) {
        launch {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                Compressor.compress(context, actualFile) {
                    upload(actualFile)
                }
            } else {
                upload(actualFile)
            }
        }
    }

    private fun upload(file: File) {
        launch {
            if (file.sizeInMb() < 25) {
                state.loading = true
                val reqFile =
                    RequestBody.create(MediaType.parse("image/${file.extension}"), file)
                val multiPartImageFile: MultipartBody.Part =
                    MultipartBody.Part.createFormData("profile-picture", file.name, reqFile)
                when (val response = repository.uploadProfilePicture(multiPartImageFile)) {
                    is RetroApiResponse.Success -> {
                        if (null != response.data.data) {
                            response.data.data?.let {
                                it.imageURL?.let { url -> state.profilePictureUrl = url }
                                SessionManager.user?.currentCustomer?.setPicture(it.imageURL)
                                Glide.with(context)
                                    .load(it.imageURL).preload()
                                state.fullName =
                                    SessionManager.user?.currentCustomer?.getFullName() ?: ""
                                state.nameInitialsVisibility = VISIBLE
                                state.loading = false
                            }
                        }
                        file.deleteRecursively()
                    }

                    is RetroApiResponse.Error -> {
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                        state.fullName =
                            SessionManager.user?.currentCustomer?.getFullName() ?: ""
                        state.nameInitialsVisibility = GONE
                        state.loading = false
                        file.deleteRecursively()
                    }
                }
            } else {
                state.toast = "File size not supported^${AlertType.DIALOG.name}"
                file.deleteRecursively()
            }
        }
    }

    override fun requestProfileDocumentsInformation() {
        launch {
            state.loading = true
            when (val response = repository.getMoreDocumentsByType("EMIRATES_ID")) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.document =
                        response.data.data?.customerDocuments?.get(0)?.documentInformation

                    val data = response.data
                    if (data.data?.dateExpiry != null) {
                        getExpiryDate(data.data?.dateExpiry ?: "")
                        trackEvent(KYCEvents.EID_EXPIRE_DATE.type)
                        trackEventWithAttributes(
                            SessionManager.user,
                            eidExpireDate = getFormattedDate(data.data?.dateExpiry ?: "")
                        )
                    } else {
                        SessionManager.eidStatus =
                            EIDStatus.NOT_SET
                        state.isShowErrorIcon.set(true)
                    }
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    if (response.error.statusCode == 400 || response.error.actualCode == "1073")
                        state.isShowErrorIcon.set(true)
                    SessionManager.eidStatus =
                        EIDStatus.NOT_SET  //set the document is required if not found
                    state.loading = false
                }
            }
        }
    }

    private fun getExpiryDate(expiryDateString: String) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getDefault()
        val expiryDate = simpleDateFormat.parse(expiryDateString)
        val cal = Calendar.getInstance()
        val currentDay = simpleDateFormat.format(cal.time)
        val currentDayDate = simpleDateFormat.parse(currentDay)
        SessionManager.eidStatus =
            when {
                expiryDate < currentDayDate -> {
                    state.isShowErrorIcon.set(true)
                    EIDStatus.EXPIRED
                }
                else -> {
                    state.isShowErrorIcon.set(false)
                    EIDStatus.VALID
                }
            }
    }

    override fun requestRemoveProfilePicture(apiRes: (Boolean) -> Unit) {
        launch {
            state.loading = true
            when (val response = repository.removeProfilePicture()) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    SessionManager.user?.currentCustomer?.setPicture("")
                    apiRes.invoke(true)
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    apiRes.invoke(false)
                }
            }
        }
    }
}
