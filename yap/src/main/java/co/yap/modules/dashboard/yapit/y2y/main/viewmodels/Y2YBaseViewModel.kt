package co.yap.modules.dashboard.yapit.y2y.main.viewmodels

import android.app.Application
import co.yap.modules.dashboard.yapit.y2y.main.interfaces.IY2Y
import co.yap.sendmoney.base.SMFeeViewModel
import co.yap.yapcore.IBase

abstract class Y2YBaseViewModel<S : IBase.State>(application: Application) :
    SMFeeViewModel<S>(application) {
    var parentViewModel: IY2Y.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        val VISIBLE: Int = 0x00000000
        val GONE: Int = 0x00000008
        if (visibility) {
            parentViewModel?.state?.tootlBarVisibility = VISIBLE

        } else {
            parentViewModel?.state?.tootlBarVisibility = GONE

        }
    }

    fun setRightButtonVisibility(visibility: Boolean) {
        parentViewModel?.state?.rightButtonVisibility = visibility
    }

    fun setLeftButtonVisibility(visibility: Boolean) {
        parentViewModel?.state?.leftButtonVisibility = visibility
    }

}