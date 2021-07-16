package co.yap.modules.dashboard.cards.reordercard.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.reordercard.interfaces.IReorderCard
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class ReorderCardBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IReorderCard.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }

}