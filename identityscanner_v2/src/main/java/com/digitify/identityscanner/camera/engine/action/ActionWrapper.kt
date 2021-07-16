package com.digitify.identityscanner.camera.engine.action

import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * A simple wrapper around a [BaseAction].
 * This can be used to add functionality around a base action.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class ActionWrapper : BaseAction() {

    /**
     * Should return the wrapped action.
     * @return the wrapped action
     */
    abstract val action: BaseAction

    override fun onStart(holder: ActionHolder) {
        super.onStart(holder)
        action.addCallback(object : ActionCallback {
            override fun onActionStateChanged(action: Action, state: Int) {
                this@ActionWrapper.state = state
                if (state == Action.Companion.STATE_COMPLETED) {
                    action.removeCallback(this)
                }
            }
        })
        action.onStart(holder)
    }

    override fun onAbort(holder: ActionHolder) {
        super.onAbort(holder)
        action.onAbort(holder)
    }

    override fun onCaptureStarted(holder: ActionHolder, request: CaptureRequest) {
        super.onCaptureStarted(holder, request)
        action.onCaptureStarted(holder, request)
    }

    override fun onCaptureProgressed(holder: ActionHolder,
                                     request: CaptureRequest,
                                     result: CaptureResult) {
        super.onCaptureProgressed(holder, request, result)
        action.onCaptureProgressed(holder, request, result)
    }

    override fun onCaptureCompleted(holder: ActionHolder,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult) {
        super.onCaptureCompleted(holder, request, result)
        action.onCaptureCompleted(holder, request, result)
    }
}
