package com.digitify.identityscanner.camera.engine.lock

import android.os.Build
import androidx.annotation.RequiresApi
import com.digitify.identityscanner.camera.engine.action.Action

import com.digitify.identityscanner.camera.engine.action.ActionHolder
import com.digitify.identityscanner.camera.engine.action.BaseAction

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class BaseLock : BaseAction() {

    override fun onStart(holder: ActionHolder) {
        super.onStart(holder)
        val isSkipped = checkShouldSkip(holder)
        val isSupported = checkIsSupported(holder)
        if (isSupported && !isSkipped) {
            onStarted(holder)
        } else {
            state = Action.STATE_COMPLETED
        }
    }

    protected abstract fun onStarted(holder: ActionHolder)

    protected abstract fun checkShouldSkip(holder: ActionHolder): Boolean

    protected abstract fun checkIsSupported(holder: ActionHolder): Boolean
}
