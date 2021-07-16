package co.yap.yapcore.defaults

import android.app.Application
import co.yap.yapcore.BaseViewModel

open class DefaultViewModel(application: Application) : BaseViewModel<IDefault.State>(application), IDefault.ViewModel {
    override val state: IDefault.State
        get() = DefaultState()
}