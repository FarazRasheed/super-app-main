package com.yap.yapandroid.modules.login.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yap.yapandroid.modules.login.interfaces.IOtpBlockedInfo
import com.yap.yapandroid.modules.login.states.OtpBlockedInfoState
import co.yap.networking.authentication.AuthApi
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.managers.SessionManager

class OtpBlockedInfoViewModel(application: Application) :
    BaseViewModel<IOtpBlockedInfo.State>(application),
    IOtpBlockedInfo.ViewModel, IRepositoryHolder<MessagesRepository> {
    override val state: IOtpBlockedInfo.State = OtpBlockedInfoState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val authRepository: AuthApi = AuthRepository
    override val onHelpNoSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override val repository: MessagesRepository = MessagesRepository

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        state.userFirstName.set(SessionManager.user?.currentCustomer?.firstName)
    }

    override fun getHelpPhoneNo() {
        launch {
            state.loading = true
            when (val response =
                repository.getHelpDeskContact()) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    response.data.data?.let { state.helpPhoneNo.set(it) }
                    onHelpNoSuccess.value = true
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                }
            }
        }
    }

    override fun getJwtToken() {
        launch {
            state.token.set(authRepository.getJwtToken())
        }
    }
}