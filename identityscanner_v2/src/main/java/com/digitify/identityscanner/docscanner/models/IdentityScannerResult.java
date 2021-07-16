package com.digitify.identityscanner.docscanner.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.digitify.identityscanner.models.Error;

public class IdentityScannerResult implements Parcelable {

    private Document document;
    private Identity identity;
    private String mrzText;
    private Error error;

    public IdentityScannerResult() {
        identity = new Identity();
        document = new Document();
        mrzText = "";
        error = null;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getMrzText() {
        return mrzText;
    }

    public void setMrzText(String mrzText) {
        this.mrzText = mrzText;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.document, flags);
        dest.writeParcelable(this.identity, flags);
        dest.writeString(this.mrzText);
        dest.writeParcelable(this.error, flags);
    }

    protected IdentityScannerResult(Parcel in) {
        this.document = in.readParcelable(Document.class.getClassLoader());
        this.identity = in.readParcelable(Identity.class.getClassLoader());
        this.mrzText = in.readString();
        this.error = in.readParcelable(Error.class.getClassLoader());
    }

    public static final Creator<IdentityScannerResult> CREATOR = new Creator<IdentityScannerResult>() {
        @Override
        public IdentityScannerResult createFromParcel(Parcel source) {
            return new IdentityScannerResult(source);
        }

        @Override
        public IdentityScannerResult[] newArray(int size) {
            return new IdentityScannerResult[size];
        }
    };
}
