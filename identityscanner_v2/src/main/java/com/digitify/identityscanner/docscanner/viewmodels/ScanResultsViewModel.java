package com.digitify.identityscanner.docscanner.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.docscanner.interfaces.IScanResults;
import com.digitify.identityscanner.docscanner.models.DocumentImage;
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult;
import com.digitify.identityscanner.docscanner.states.ScanResultsState;
import com.digitify.identityscanner.utils.ImageUtils;
import com.digitify.identityscanner.base.BaseAndroidViewModel;

import java.util.ArrayList;

public class ScanResultsViewModel extends BaseAndroidViewModel implements IScanResults.ViewModel {

    private IScanResults.View view;
    private IdentityScannerResult scanResults;
    private ScanResultsState state;

    public ScanResultsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(IdentityScannerResult scanResults, IScanResults.View view) {
        this.scanResults = scanResults;
        this.view = view;

        setupState();
    }

    private void setupState() {
        getState().reset();
        getState().setResult(getScanResults());

        // Find the front side of document
        ArrayList<DocumentImage> files = getScanResults().getDocument().getFiles();
        DocumentImage front = null;
        for (DocumentImage img: files) {
            if (img.getType() == DocumentPageType.FRONT) {
                front = img;
                break;
            }
        }

        if (front != null) {
            Bitmap bitmap = ImageUtils.getBitmapFromStorage(front.getOriginalFile());
            getState().setFaceBitmap(bitmap);
        }
    }

    @Override
    public IScanResults.View getView() {
        return view;
    }

    @Override
    public ScanResultsState getState() {
        if (state == null) state = new ScanResultsState(getContext());
        return state;
    }

    public IdentityScannerResult getScanResults() {
        return scanResults;
    }
}
