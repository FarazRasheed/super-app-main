package co.yap.modules.frame

import android.app.Application
import co.yap.yapcore.BaseViewModel

open class FrameActivityViewModel(application: Application):
    BaseViewModel<IFrameActivity.State>(application),
    IFrameActivity.ViewModel {
    override val state: FrameActivityState = FrameActivityState()
}