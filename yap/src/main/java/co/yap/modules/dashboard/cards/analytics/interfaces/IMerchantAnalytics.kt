package co.yap.modules.dashboard.cards.analytics.interfaces

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.cards.analytics.main.interfaces.ICardAnalyticsMain
import co.yap.yapcore.IBase

interface IMerchantAnalytics {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }
    interface ViewModel : IBase.ViewModel<State>{
    }
    interface State : IBase.State
}