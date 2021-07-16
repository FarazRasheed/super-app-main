package com.digitify.identityscanner.docscanner.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public abstract class Cropper implements ICropper {

    @Override
    public final void crop(Activity act, String filepath) {
        CropImage.activity(Uri.fromFile(new File(filepath)))
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(act);
    }

    @Override
    public final void crop(Context context, Fragment fragment, String filepath) {
        CropImage.activity(Uri.fromFile(new File(filepath)))
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(context, fragment);
    }

    @Override
    public final void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                handleCroppedPicture(result);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                onCropFailed(result.getError());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                onCropFailed(new Exception("Operation Cancelled"));
            }
        }
    }

    private void handleCroppedPicture(CropImage.ActivityResult result) {
        try {
            onCropped(result.getUri());
        } catch (Exception e) {
            onCropFailed(e);
        }
    }
}
