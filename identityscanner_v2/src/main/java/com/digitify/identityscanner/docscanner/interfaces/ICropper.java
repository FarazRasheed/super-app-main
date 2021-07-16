package com.digitify.identityscanner.docscanner.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface ICropper {
    void crop(Activity act, String filepath);

    void crop(Context context, Fragment fragment, String filepath);

    void onCropped(Uri uri);

    void onCropFailed(Exception e);

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}
