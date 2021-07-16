package com.digitify.identityscanner.docscanner.states;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.databinding.Bindable;

import com.digitify.identityscanner.R;
import com.digitify.identityscanner.BR;
import com.digitify.identityscanner.models.KeyValue;
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult;
import com.digitify.identityscanner.base.State;

import java.util.ArrayList;

public class ScanResultsState extends State {

    private Bitmap faceBitmap;
    private IdentityScannerResult result;
    private ArrayList<KeyValue> documentInfo;
    private ArrayList<KeyValue> personalInfo;
    private Context context;

    public ScanResultsState(Context context) {
        this.context = context;
    }

    private void prepareDocumentInfo(IdentityScannerResult result) {
        getDocumentInfo().add(new KeyValue(getContext().getResources().getString(R.string.document_type_title), result.getDocument().getType().toString()));
        getDocumentInfo().add(new KeyValue(getContext().getResources().getString(R.string.issuing_country), result.getIdentity().getIssuingCountry()));
        getDocumentInfo().add(new KeyValue(getContext().getResources().getString(R.string.exp_date), result.getIdentity().getExpirationDate().toString()));
        getDocumentInfo().add(new KeyValue(getContext().getResources().getString(R.string.doc_number), result.getIdentity().getDocumentNumber()));
        getDocumentInfo().add(new KeyValue(getContext().getResources().getString(R.string.citizen_number), result.getIdentity().getCitizenNumber()));
    }

    private void preparePersonalInfo(IdentityScannerResult result) {
        getPersonalInfo().add(new KeyValue(getContext().getResources().getString(R.string.first_name), result.getIdentity().getGivenName()));
        getPersonalInfo().add(new KeyValue(getContext().getResources().getString(R.string.sir_name), result.getIdentity().getSirName()));
        getPersonalInfo().add(new KeyValue(getContext().getResources().getString(R.string.dob), result.getIdentity().getDateOfBirth().toString()));
        getPersonalInfo().add(new KeyValue(getContext().getResources().getString(R.string.gender), String.valueOf(result.getIdentity().getGender())));
        getPersonalInfo().add(new KeyValue(getContext().getResources().getString(R.string.nationality), result.getIdentity().getNationality()));
    }

    @Override
    public void reset() {
        result = null;
        faceBitmap = null;
        getDocumentInfo().clear();
        getPersonalInfo().clear();
    }

    @Bindable
    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
        notifyPropertyChanged(BR.faceBitmap);
    }

    @Bindable
    public IdentityScannerResult getResult() {
        return result;
    }

    public void setResult(IdentityScannerResult result) {
        this.result = result;
        prepareDocumentInfo(result);
        preparePersonalInfo(result);
        notifyPropertyChanged(com.digitify.identityscanner.BR.result);
    }

    @Bindable
    public ArrayList<KeyValue> getDocumentInfo() {
        if (documentInfo == null) documentInfo = new ArrayList<>();
        return documentInfo;
    }

    @Bindable
    public ArrayList<KeyValue> getPersonalInfo() {
        if (personalInfo == null) personalInfo = new ArrayList<>();
        return personalInfo;
    }

    public Context getContext() {
        return context;
    }
}
