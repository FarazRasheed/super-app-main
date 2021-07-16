package com.digitify.identityscanner.camera.engine.meter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.digitify.identityscanner.camera.engine.action.ActionHolder;
import com.digitify.identityscanner.camera.engine.action.ActionWrapper;
import com.digitify.identityscanner.camera.engine.action.Actions;
import com.digitify.identityscanner.camera.engine.action.BaseAction;
import com.digitify.identityscanner.camera.engine.lock.ExposureLock;
import com.digitify.identityscanner.camera.engine.lock.FocusLock;
import com.digitify.identityscanner.camera.engine.lock.WhiteBalanceLock;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class MeterResetAction extends ActionWrapper {

    private final BaseAction action;

    public MeterResetAction() {
        this.action = Actions.INSTANCE.together(
                new ExposureReset(),
                new FocusReset(),
                new WhiteBalanceReset()
        );
    }

    @NonNull
    @Override
    public BaseAction getAction() {
        return action;
    }
}
