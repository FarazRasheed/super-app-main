package com.digitify.identityscanner.docscanner.interfaces;

import android.view.View;

import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.docscanner.enums.DocumentType;
import com.digitify.identityscanner.docscanner.states.DocReviewState;
import com.digitify.identityscanner.interfaces.IBase;

import co.yap.yapcore.SingleClickEvent;

public interface IDocReview {

    interface View extends IBase.View {
        void requestCancel();

        void requestDone();

        void requestRetake();
    }

    interface ViewModel extends IBase.ViewModel {
        IDocReview.View getView();

        void init(String filepath, DocumentType docType, DocumentPageType type, IDocReview.View view);

        void handleClickOnCancel();

        void handleClickOnDone(android.view.View v);

        void handleClickOnRetake();

        DocReviewState getState();

        SingleClickEvent getSingleClickEvent();
//        val clickEvent: SingleClickEvent
    }
}
