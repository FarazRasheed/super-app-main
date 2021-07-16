package co.yap.modules.frame

import co.yap.yapcore.IBase

interface IFrameActivity {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State
}