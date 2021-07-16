package com.digitify.identityscanner.camera.engine.action

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.os.Build

import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi

import java.util.ArrayList

/**
 * The base implementation of [Action] that should always be subclassed,
 * instead of implementing the root interface itself.
 *
 * It holds a list of callbacks and dispatches events to them, plus it cares about
 * its own lifecycle:
 * - when [.start] is called, we add ourselves to the holder list
 * - when [.STATE_COMPLETED] is reached, we remove ouverselves from the holder list
 *
 * This is very important in all cases.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class BaseAction : Action {

    private val callbacks = ArrayList<ActionCallback>()
    /**
     * Called by subclasses to notify of their state. If state is [.STATE_COMPLETED],
     * this removes this action from the holder.
     * @param newState new state
     */
    override var state: Int = 0
        protected set(newState) {
            if (newState != this.state) {
                field = newState
                for (callback in callbacks) {
                    callback.onActionStateChanged(this, this.state)
                }
                if (this.state == Action.Companion.STATE_COMPLETED) {
                    holder!!.removeAction(this)
                    onCompleted(holder!!)
                }
            }
        }
    /**
     * Returns the holder.
     * @return the holder
     */
    protected var holder: ActionHolder? = null
        private set

    /**
     * Whether this action has reached the completed state.
     * @return true if completed
     */
    val isCompleted: Boolean
        get() = this.state == Action.Companion.STATE_COMPLETED

    override fun start(holder: ActionHolder) {
        holder.addAction(this)
        onStart(holder)
    }

    override fun abort(holder: ActionHolder) {
        holder.removeAction(this)
        if (!isCompleted) {
            onAbort(holder)
            state = Action.Companion.STATE_COMPLETED
        }
    }

    /**
     * Action was started and will soon receive events from the
     * holder stream.
     * @param holder holder
     */
    @CallSuper
    open fun onStart(holder: ActionHolder) {
        this.holder = holder // must be here
        // Overrideable
    }

    /**
     * Action was aborted and will not receive events from the
     * holder stream anymore. It will soon be marked as completed.
     * @param holder holder
     */
    open fun onAbort(holder: ActionHolder) {
        // Overrideable
    }

    override fun onCaptureStarted(holder: ActionHolder, request: CaptureRequest) {
        // Overrideable
    }

    override fun onCaptureProgressed(holder: ActionHolder,
                                     request: CaptureRequest,
                                     result: CaptureResult) {
        // Overrideable
    }

    override fun onCaptureCompleted(holder: ActionHolder,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult) {
        // Overrideable
    }

    /**
     * Called when this action has completed (possibly aborted).
     * @param holder holder
     */
    protected open fun onCompleted(holder: ActionHolder) {
        // Overrideable
    }


    /**
     * Reads a characteristic with a fallback.
     * @param key key
     * @param fallback fallback
     * @param <T> key type
     * @return value or fallback
    </T> */
    protected fun <T> readCharacteristic(key: CameraCharacteristics.Key<T>,
                                         fallback: T): T {
        val value = holder!!.getCharacteristics(this).get(key)
        return value ?: fallback
    }

    override fun addCallback(callback: ActionCallback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
            callback.onActionStateChanged(this, state)
        }
    }

    override fun removeCallback(callback: ActionCallback) {
        callbacks.remove(callback)
    }
}
