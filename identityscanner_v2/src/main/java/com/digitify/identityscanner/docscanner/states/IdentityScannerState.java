package com.digitify.identityscanner.docscanner.states;

import androidx.databinding.Bindable;

import com.digitify.identityscanner.BR;
import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.base.State;

public class IdentityScannerState extends State {
    private boolean isLoading;
    private boolean isProcessing;
    private DocumentPageType scanMode;

    public IdentityScannerState() {
        this.isLoading = false;
        this.isProcessing = false;
        setScanMode(DocumentPageType.FRONT);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(com.digitify.identityscanner.BR.loading);
    }

    @Bindable
    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
        notifyPropertyChanged(BR.processing);
    }

    @Bindable
    public DocumentPageType getScanMode() {
        return scanMode;
    }

    public void setScanMode(DocumentPageType scanMode) {
        this.scanMode = scanMode;
        notifyPropertyChanged(BR.scanMode);
    }

    public void reset() {
        this.isLoading = false;
        this.isProcessing = false;
        setScanMode(DocumentPageType.FRONT);
    }
}
