package co.yap.modules.dashboard.cards.analytics.interfaces

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.cards.analytics.main.interfaces.ICardAnalyticsMain
import co.yap.modules.dashboard.cards.analytics.main.viewmodels.CardAnalyticsMainViewModel
import co.yap.yapcore.IBase

interface ICategoryAnalytics {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
    }

    interface State : IBase.State
}