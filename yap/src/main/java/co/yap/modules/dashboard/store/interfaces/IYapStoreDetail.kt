package co.yap.modules.dashboard.store.interfaces

import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IYapStoreDetail {

    interface View : IBase.View<ViewModel> {
        var testValue: String
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var yapStoreData: MutableList<Store>
        fun handlePressOnView(id: Int)
    }

    interface State : IBase.State {
        var title: String
        var subTitle: String
        var image: Int
        var storeIcon: Int

        var storeHeading: String
        var storeDetail: String
    }
}