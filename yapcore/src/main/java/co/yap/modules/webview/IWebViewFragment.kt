package co.yap.modules.webview

import co.yap.yapcore.IBase

interface IWebViewFragment {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State
}