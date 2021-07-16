package com.digitify.identityscanner.docscanner.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.docscanner.enums.DocumentType;
import com.digitify.identityscanner.docscanner.interfaces.IDocReview;
import com.digitify.identityscanner.docscanner.states.DocReviewState;
import com.digitify.identityscanner.utils.ImageUtils;
import com.digitify.identityscanner.base.BaseAndroidViewModel;

import co.yap.translation.Strings;
import co.yap.yapcore.SingleClickEvent;

public class DocReviewViewModel extends BaseAndroidViewModel implements IDocReview.ViewModel {
    private String filepath;
    private DocumentPageType pageType;
    private DocumentType docType;
    private IDocReview.View view;
    private DocReviewState state;
    public boolean isDone =false;

    public DocReviewViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public IDocReview.View getView() {
        return view;
    }

    @Override
    public void init(String filepath, DocumentType docType, DocumentPageType pageType, IDocReview.View view) {
        this.filepath = filepath;
        this.pageType = pageType;
        this.docType = docType;
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initiateReview(getFilepath());
    }

    @Override
    public SingleClickEvent getSingleClickEvent() {
        return new SingleClickEvent();
    }

    private void initiateReview(String path) {
        if (!ImageUtils.isValidFile(path)) {
            handleClickOnRetake();
            return;
        }

        Bitmap bitmap = ImageUtils.getBitmapFromStorage(filepath);
        getState().setPreviewBitmap(bitmap);

        // Detect Face and MRZ based on Document type
        if (getDocType() == DocumentType.EID) {
            // 2 sided doc. Check which side is it.

            if (getPageType() == DocumentPageType.FRONT) {
                // check face

                onFaceValidationComplete(bitmap, bitmap != null);
            } else if (getPageType() == DocumentPageType.BACK) {
                // check face
//                Mrz mrz = detectMrz(bitmap);
                getState().setLoading(false);
                getState().setDocValid(true);
                getState().setReviewText(getString(Strings.idenetity_scanner_sdk_screen_review_info_display_text_review));
//                onMrzValidationComplete(bitmap, mrz != null);
            }

        } else if (getDocType() == DocumentType.PASSPORT) {
            //TODO Implement DocumentType.PASSPORT here

        } else {
            handleClickOnRetake();
        }
    }

    private void onFaceValidationComplete(Bitmap bm, boolean isValidated) {
        getState().setDocValid(isValidated);
        getState().setReviewText(isValidated ? getString(Strings.idenetity_scanner_sdk_screen_review_info_display_text_review) : getString(Strings.idenetity_scanner_sdk_screen_review_info_display_text_error_face));
    }


    @Override
    public void handleClickOnCancel() {
        getView().requestCancel();
    }

    @Override
    public void handleClickOnDone(View v) {

        getView().requestDone();
        isDone = true;
    }

    @Override
    public void handleClickOnRetake() {
        isDone = false;
        getView().requestRetake();
    }

    @Override
    public DocReviewState getState() {
        if (state == null) state = new DocReviewState();
        return state;
    }

    public String getFilepath() {
        return filepath;
    }

    public DocumentPageType getPageType() {
        return pageType;
    }

    public DocumentType getDocType() {
        return docType;
    }

}
