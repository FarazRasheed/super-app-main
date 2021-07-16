package co.yap.modules.dashboard.yapit.y2y.home.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IYapToYap {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val recentTransferData: MutableLiveData<List<Beneficiary>>
        fun handlePressOnView(id: Int)
        fun getRecentBeneficiaries()

    }

    interface State : IBase.State {

    }
}