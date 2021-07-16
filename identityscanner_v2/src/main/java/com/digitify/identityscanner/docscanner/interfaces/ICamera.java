package com.digitify.identityscanner.docscanner.interfaces;

import android.graphics.Bitmap;

import com.digitify.identityscanner.core.arch.SingleLiveEvent;
import com.digitify.identityscanner.interfaces.IBase;
import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.docscanner.enums.DocumentType;
import com.digitify.identityscanner.docscanner.states.CameraState;

import java.io.File;


public interface ICamera {

    interface View extends IBase.View {
        void openCropper(String filename);

        void setInstructions(String inst);

        void onCaptureProcessCompleted(String filename);
    }

    interface ViewModel extends IBase.ViewModel {
        void setDocumentType(DocumentType documentType);

        void setScanMode(DocumentPageType mode);

        DocumentPageType getScanMode();

        void reset();

        //void setTitle(String title);
//
        void setInstructions(String inst);
//
//        void processFrame(Mat frame);
//
        void handleOnPressCapture(File file);
//
//        void handleOnPressQuickCapture(Mat snapshot);

        CameraState getState();

        SingleLiveEvent<String> getCapturedImage();

        SingleLiveEvent<String> getCapturedDocument();

        void setCapturedImage(String filename);

        void setCapturedDocument(String filename);
    }

}
