package co.yap.modules.forgotpasscode.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IForgotPasscodeSuccess {
    interface View:IBase.View<ViewModel>
    interface ViewModel:IBase.ViewModel<State>{
        val handlePressOnButtonEvent:SingleClickEvent
        fun handlePressOnGoToDashboardButton(id:Int)

    }
    interface State:IBase.State{
        var title:String
        var subTitle:String
        var buttonTitle:String
    }
}