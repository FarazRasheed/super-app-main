package com.digitify.identityscanner.interfaces;

import android.content.Context;

public interface IBase {

    interface ViewModel extends ILIfeCycle {

    }

    interface View {
        void showToast(String message);

        Context getContext();
    }
}
