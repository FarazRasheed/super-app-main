package co.yap.modules.dashboard.cards.analytics.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import co.yap.modules.dashboard.cards.analytics.interfaces.ICategoryAnalytics
import co.yap.modules.dashboard.cards.analytics.main.viewmodels.CardAnalyticsBaseViewModel
import co.yap.modules.dashboard.cards.analytics.states.CategoryAnalyticsState

class CategoryAnalyticsViewModel(application: Application) :
    CardAnalyticsBaseViewModel<ICategoryAnalytics.State>(application),
    ICategoryAnalytics.ViewModel {

    override val state: CategoryAnalyticsState = CategoryAnalyticsState()
}