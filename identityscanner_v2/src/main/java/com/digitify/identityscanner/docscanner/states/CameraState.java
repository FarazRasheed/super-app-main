package com.digitify.identityscanner.docscanner.states;

import androidx.databinding.Bindable;

import com.digitify.identityscanner.BR;
import com.digitify.identityscanner.base.State;


public class CameraState extends State {
    private String title;
    private String instructions;
    private String stepInstructions;
    private String submitButtonTitle;
    private boolean capturing;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
        notifyPropertyChanged(BR.instructions);
    }

    @Bindable
    public String getStepInstructions() {
        return stepInstructions;
    }

    public void setStepInstructions(String stepInstructions) {
        this.stepInstructions = stepInstructions;
        notifyPropertyChanged(BR.stepInstructions);
    }

    @Bindable
    public String getSubmitButtonTitle() {
        return submitButtonTitle;
    }

    public void setSubmitButtonTitle(String submitButtonTitle) {
        this.submitButtonTitle = submitButtonTitle;
        notifyPropertyChanged(BR.submitButtonTitle);
    }

    @Bindable
    public boolean isCapturing() {
        return capturing;
    }

    public void setCapturing(boolean capturing) {
        this.capturing = capturing;
    }

    @Override
    public void reset() {
        title = "";
        instructions = "";
        stepInstructions = "";
        capturing = false;
        notifyChange();
    }
}
