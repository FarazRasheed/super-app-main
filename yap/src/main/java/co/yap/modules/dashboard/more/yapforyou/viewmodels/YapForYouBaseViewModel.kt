package co.yap.modules.dashboard.more.yapforyou.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.yapforyou.interfaces.IYapForYouMain
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class YapForYouBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {

    var parentViewModel: YapForYouMainViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }
    fun setLeftIcon(icon : Int){
        parentViewModel?.state?.leftIcon?.set(icon)
    }
    fun setLeftIconVisibility(visibility : Boolean){
        parentViewModel?.state?.leftIconVisibility?.set(visibility)
    }
}
