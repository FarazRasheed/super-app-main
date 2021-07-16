package co.yap.modules.onboarding.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IWaitingList {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnNext(id: Int)
    }

    interface State : IBase.State {
        var rankNoInList: MutableLiveData<String>?
    }
}