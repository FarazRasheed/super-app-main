package co.yap.modules.dashboard.more.help.interfaces

import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import co.yap.networking.authentication.AuthApi
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHelpSupport {
    interface State : IBase.State {
        var contactPhone: ObservableField<String>
        var FaqsUrl: ObservableField<String>
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val urlUpdated: MutableLiveData<String>
        val authRepository: AuthApi
        fun handlePressOnView(id: Int)
        fun getHelpDeskPhone()
        fun getFaqsUrl()
    }

    interface View : IBase.View<ViewModel> {
        fun getBinding(): ViewDataBinding
    }
}