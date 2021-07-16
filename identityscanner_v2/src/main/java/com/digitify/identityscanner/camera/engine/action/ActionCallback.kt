package com.digitify.identityscanner.camera.engine.action

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * A callback for [Action] state changes.
 * See the action class.
 *
 * See also [CompletionCallback].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
interface ActionCallback {

    /**
     * Action state has just changed.
     * @param action action
     * @param state new state
     */
    fun onActionStateChanged(action: Action, state: Int)
}
