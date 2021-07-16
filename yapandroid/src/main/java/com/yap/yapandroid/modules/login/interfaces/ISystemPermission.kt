package com.yap.yapandroid.modules.login.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent

interface ISystemPermission {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var screenType: String
        fun permissionGranted()
        fun permissionNotGranted()
        fun handlePressOnTermsAndConditions(id:Int)
        val permissionGrantedPressEvent: SingleLiveEvent<Boolean>
        val permissionNotGrantedPressEvent: SingleLiveEvent<Boolean>
        val handlePressOnTermsAndConditionsPressEvent: SingleClickEvent
    }

    interface State : IBase.State {
        var icon: Int
        var title: String
        var subTitle: String
        var termsAndConditionsVisibility: Boolean
        var buttonTitle: String
        var denied: String
    }
}