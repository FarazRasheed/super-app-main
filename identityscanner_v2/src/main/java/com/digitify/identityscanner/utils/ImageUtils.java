package com.digitify.identityscanner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.File;

import static android.graphics.BitmapFactory.decodeFile;
import static co.yap.yapcore.helpers.extentions.FileExtentionsKt.createTempFile;

public class ImageUtils {

    public static boolean isValidFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;
        return new File(filePath).exists();
    }

    public static Bitmap getBitmapFromStorage(String filePath) {
        return decodeFile(filePath);
    }

    @Nullable
    public static File getFilePrivately(Context context) {
        return createTempFile(context, "jpg");
    }
}