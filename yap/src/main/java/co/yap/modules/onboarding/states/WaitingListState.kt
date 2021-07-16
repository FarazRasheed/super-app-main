package co.yap.modules.onboarding.states

import androidx.lifecycle.MutableLiveData
import co.yap.modules.onboarding.interfaces.IWaitingList
import co.yap.yapcore.BaseState

class WaitingListState : BaseState(), IWaitingList.State {
    override var rankNoInList: MutableLiveData<String>? = MutableLiveData()
}
