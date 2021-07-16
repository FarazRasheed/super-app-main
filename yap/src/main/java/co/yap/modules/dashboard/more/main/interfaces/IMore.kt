package co.yap.modules.dashboard.more.main.interfaces

import co.yap.networking.customers.responsedtos.documents.GetMoreDocumentsResponse
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleLiveEvent

interface IMore {
    interface State : IBase.State {
        var tootlBarVisibility: Int
        var tootlBarBadgeVisibility: Boolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnTickButton()
        var BadgeVisibility: Boolean
        var document: GetMoreDocumentsResponse.Data.CustomerDocument.DocumentInformation?
    }

    interface View : IBase.View<ViewModel>
}