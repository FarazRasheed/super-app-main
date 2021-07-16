package com.digitify.identityscanner.core.arch;

import android.os.Handler;
import android.os.Looper;

public class MainHandler {
    public static Handler get() {
        return new Handler(Looper.getMainLooper());
    }
}
