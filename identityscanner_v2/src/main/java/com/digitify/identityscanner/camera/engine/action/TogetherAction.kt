package com.digitify.identityscanner.camera.engine.action

import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.os.Build
import androidx.annotation.RequiresApi

import java.util.ArrayList

/**
 * Performs a list of actions together, completing
 * once all of them have completed.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class TogetherAction(actions: List<BaseAction>) : BaseAction() {
    // Need to be BaseAction so we can call onStart() instead of start()
    private val actions: List<BaseAction>
    private val runningActions: MutableList<BaseAction>

    init {
        this.actions = ArrayList(actions)
        this.runningActions = ArrayList(actions)
        for (action in actions) {
            action.addCallback(object : ActionCallback {
                override fun onActionStateChanged(action: Action, state: Int) {
                    if (state == Action.Companion.STATE_COMPLETED) {

                        runningActions.remove(action)
                    }
                    if (runningActions.isEmpty()) {
                        this@TogetherAction.state = Action.STATE_COMPLETED
                    }
                }
            })
        }
    }

    override fun onStart(holder: ActionHolder) {
        super.onStart(holder)
        for (action in actions) {
            if (!action.isCompleted) action.onStart(holder)
        }
    }

    override fun onAbort(holder: ActionHolder) {
        super.onAbort(holder)
        for (action in actions) {
            if (!action.isCompleted) action.onAbort(holder)
        }
    }

    override fun onCaptureStarted(holder: ActionHolder, request: CaptureRequest) {
        super.onCaptureStarted(holder, request)
        for (action in actions) {
            if (!action.isCompleted) action.onCaptureStarted(holder, request)
        }
    }

    override fun onCaptureProgressed(holder: ActionHolder,
                                     request: CaptureRequest,
                                     result: CaptureResult) {
        super.onCaptureProgressed(holder, request, result)
        for (action in actions) {
            if (!action.isCompleted) action.onCaptureProgressed(holder, request, result)
        }
    }

    override fun onCaptureCompleted(holder: ActionHolder,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult) {
        super.onCaptureCompleted(holder, request, result)
        for (action in actions) {
            if (!action.isCompleted) action.onCaptureCompleted(holder, request, result)
        }
    }
}
