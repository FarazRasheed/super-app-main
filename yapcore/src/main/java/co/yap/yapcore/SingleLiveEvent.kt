package co.yap.yapcore

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val debounceDelay: Long = 500L
    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mPending.get()) {
                observer.onChanged(t)
                Handler(Looper.getMainLooper()).postDelayed({ mPending.set(false) }, debounceDelay)
            }
        })
    }

    override fun postValue(value: T) {
        if (!mPending.get()) {
            super.postValue(value)
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        if (!mPending.get()) {
            mPending.set(true)
            super.setValue(t)
        }
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {

        private val TAG = "SingleLiveEvent"
    }
}
