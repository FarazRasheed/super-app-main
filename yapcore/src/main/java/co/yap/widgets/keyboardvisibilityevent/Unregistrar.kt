package co.yap.widgets.keyboardvisibilityevent

import android.view.ViewTreeObserver


interface Unregistrar {

    /**
     * unregisters the [ViewTreeObserver.OnGlobalLayoutListener] and there by does not provide any more callback to the [KeyboardVisibilityEventListener]
     */
    fun unregister()
}

