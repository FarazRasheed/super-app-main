package com.yap.yapandroid.modules.login.interfaces

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent

interface ILogin {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val signInButtonPressEvent: SingleLiveEvent<Boolean>
        val signUpButtonPressEvent: SingleLiveEvent<Boolean>
        var isAccountBlocked:MutableLiveData<Boolean>
        fun handlePressOnLogin()
        fun handlePressOnSignUp()
        fun onEditorActionListener(): TextView.OnEditorActionListener
    }

    interface State : IBase.State {
        var email: String
        var emailError: MutableLiveData<String>
        var valid: Boolean
        var twoWayTextWatcher: String
        var drawbleRight: Drawable?
        var refreshField: Boolean
    }
}