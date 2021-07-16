package com.yap.yapandroid.modules.startup.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IAccountSelection {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id:Int)
    }

    interface State : IBase.State
}