package com.digitify.identityscanner.docscanner.interfaces;

import androidx.annotation.NonNull;

import com.digitify.identityscanner.core.arch.SingleLiveEvent;
import com.digitify.identityscanner.interfaces.IBase;
import com.digitify.identityscanner.docscanner.enums.DocumentType;
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult;
import com.digitify.identityscanner.docscanner.states.IdentityScannerState;

public interface IIdentityScanner {

    interface IView extends IBase.View {

        void init();

        void reviewDoc(String filePath);

        void scanDoc();

        void scanDocFromCamera();

//        void scanDocFromGallery();

        void finishWithResult(IdentityScannerResult result);

        void finishWithoutResult();

        void setObservers();
    }

    interface IViewModel extends IBase.ViewModel {
        void init(DocumentType documentType, IIdentityScanner.IView view);

        void onPictureTaken(@NonNull String filepath);

        void onPictureReviewFailed();

        void onPictureReviewCancelled();

        void onPictureReviewComplete(@NonNull String filepath);

        void reset();

        SingleLiveEvent<String> getErrorMessage();

        void setErrorMessage(String message);

        IIdentityScanner.IView getView();

        IdentityScannerState getState();

        DocumentType getDocumentType();

        IdentityScannerResult getScannerResult();

        void onResultsAccepted(IdentityScannerResult result);

        void onResultNotAccepted();

    }
}
