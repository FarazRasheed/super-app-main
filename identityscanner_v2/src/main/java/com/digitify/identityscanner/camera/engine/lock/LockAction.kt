package com.digitify.identityscanner.camera.engine.lock

import android.os.Build
import androidx.annotation.RequiresApi

import com.digitify.identityscanner.camera.engine.action.ActionWrapper
import com.digitify.identityscanner.camera.engine.action.Actions
import com.digitify.identityscanner.camera.engine.action.BaseAction

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LockAction : ActionWrapper() {

    override val action = Actions.together(
            ExposureLock(),
            FocusLock(),
            WhiteBalanceLock()
    )
}
