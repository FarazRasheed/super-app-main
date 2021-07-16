package com.yap.yapandroid.modules.login.viewmodels

import android.annotation.TargetApi
import android.app.Application
import android.os.Build
import com.yap.yapandroid.modules.login.states.SystemPermissionState
import com.yap.yapandroid.constants.Constants
import com.yap.yapandroid.modules.login.interfaces.ISystemPermission
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent
import com.yap.yapandroid.R

class SystemPermissionViewModel(application: Application) : BaseViewModel<ISystemPermission.State>(application),
    ISystemPermission.ViewModel {

    override val permissionGrantedPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val permissionNotGrantedPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val handlePressOnTermsAndConditionsPressEvent: SingleClickEvent = SingleClickEvent()

    override var screenType: String = ""

    override fun onCreate() {
        super.onCreate()
        setupViews()
    }

    override fun permissionGranted() {
        permissionGrantedPressEvent.value = true
    }

    override fun permissionNotGranted() {
        permissionNotGrantedPressEvent.value = true
    }

    override fun handlePressOnTermsAndConditions(id: Int) {
        handlePressOnTermsAndConditionsPressEvent.postValue(id)
    }

    fun setupViews() {
        if (screenType == Constants.TOUCH_ID_SCREEN_TYPE) touchIdViews() else notificationViews()
    }

    override val state: SystemPermissionState = SystemPermissionState()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun touchIdViews() {
        state.icon = R.drawable.ic_fingerprint
        state.title = getString(Strings.screen_system_permission_text_title)
        state.subTitle = getString(Strings.screen_system_permission_text_sub_title)
        state.termsAndConditionsVisibility = true
        state.denied = getString(Strings.screen_system_permission_text_denied)
        state.buttonTitle = getString(Strings.screen_system_permission_button_touch_id)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun notificationViews() {
        state.icon = R.drawable.ic_notification
        state.title = getString(Strings.screen_notification_permission_text_title)
        state.subTitle = getString(Strings.screen_notification_permission_text_sub_title)
        state.denied = getString(Strings.screen_system_permission_text_denied)
        state.termsAndConditionsVisibility = false
        state.buttonTitle = getString(Strings.screen_notification_permission_button_title)
    }
}