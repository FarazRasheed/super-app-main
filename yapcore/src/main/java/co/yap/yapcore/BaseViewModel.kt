package co.yap.yapcore

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import co.yap.translation.Translator
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.interfaces.CoroutineViewModel
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<S : IBase.State>(application: Application) :
    AndroidViewModel(application),
    IBase.ViewModel<S>, CoroutineViewModel {

    override val context: Context = getApplication<Application>().applicationContext
    final override val viewModelJob = Job()
    override val viewModelScope = CloseableCoroutineScope(viewModelJob + Dispatchers.Main)
    val viewModelBGScope = CloseableCoroutineScope(viewModelJob + Dispatchers.IO)

    class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
        override val coroutineContext: CoroutineContext = context
        override fun close() {
            coroutineContext.cancel()
        }
    }

    fun onToolBarClick(view: View) {
        toolBarClickEvent.setValue(view.id)
    }

    fun showToast(message: String) {
        state.toast = "${message}^${AlertType.DIALOG.name}"
    }

    fun showDialogWithCancel(message: String) {
        state.toast = "${message}^${AlertType.DIALOG_WITH_FINISH.name}"
    }

    override fun onCleared() {
        cancelAllJobs()
        super.onCleared()
    }

    override fun cancelAllJobs() {
        viewModelBGScope.close()
        viewModelScope.cancel()
        viewModelJob.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        state.init()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        state.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        state.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        state.destroy()
    }

    override fun registerLifecycleOwner(owner: LifecycleOwner?) {
        unregisterLifecycleOwner(owner)
        owner?.lifecycle?.addObserver(this)
    }

    override fun unregisterLifecycleOwner(owner: LifecycleOwner?) {
        owner?.lifecycle?.removeObserver(this)
    }

    override fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    override fun launchBG(block: suspend () -> Unit) = viewModelScope.async {
        block()

    }

    override fun getString(resourceId: Int): String = Translator.getString(context, resourceId)

    override fun getString(resourceId: String): String = Translator.getString(context, resourceId)

    override val toolBarClickEvent = SingleClickEvent()

}

