package com.digitify.identityscanner.camera.engine.action

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * A special [ActionCallback] that just checks for the
 * completed state. Handy as an inner anonymous class.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class CompletionCallback : ActionCallback {

    override fun onActionStateChanged(action: Action, state: Int) {
        if (state == Action.STATE_COMPLETED) {
            onActionCompleted(action)
        }
    }

    /**
     * The given action has just reached the completed state.
     * @param action action
     */
    protected abstract fun onActionCompleted(action: Action)
}
