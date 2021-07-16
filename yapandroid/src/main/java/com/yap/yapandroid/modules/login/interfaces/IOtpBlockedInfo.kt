package com.yap.yapandroid.modules.login.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.authentication.AuthApi
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IOtpBlockedInfo {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        val authRepository: AuthApi
        val onHelpNoSuccess: MutableLiveData<Boolean>
        fun handlePressOnView(id: Int)
        fun getHelpPhoneNo()
        fun getJwtToken()
    }

    interface State : IBase.State {
        val userFirstName: ObservableField<String>
        val helpPhoneNo: ObservableField<String>
        var token: ObservableField<String>
    }
}