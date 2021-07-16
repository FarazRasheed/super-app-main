package co.yap.modules.dashboard.cards.analytics.main.viewmodels

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class CardAnalyticsBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: CardAnalyticsMainViewModel? = CardAnalyticsMainViewModel(application)

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }

    fun setSelectedDate(date: String) {
        parentViewModel?.state?.currentSelectedMonth = date
    }
}