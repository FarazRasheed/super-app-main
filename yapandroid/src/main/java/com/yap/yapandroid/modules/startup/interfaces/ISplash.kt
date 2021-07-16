package com.yap.yapandroid.modules.startup.interfaces

import co.yap.networking.customers.responsedtos.AppUpdate
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent

interface ISplash {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val splashComplete: SingleLiveEvent<Boolean>
        var appUpdate: SingleLiveEvent<AppUpdate>
        fun getAppUpdate()
    }

    interface State : IBase.State
}