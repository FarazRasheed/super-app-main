package co.yap.modules.webview

import android.app.Application
import co.yap.yapcore.BaseViewModel

class WebViewFragmentViewModel(application: Application) :
    BaseViewModel<IWebViewFragment.State>(application = application), IWebViewFragment.ViewModel {
    override val state: WebViewFragmentState = WebViewFragmentState()
}