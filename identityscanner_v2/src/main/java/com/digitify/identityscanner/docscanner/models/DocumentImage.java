package com.digitify.identityscanner.docscanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.digitify.identityscanner.docscanner.enums.DocumentPageType;

public class DocumentImage implements Parcelable {

    private String originalFile, croppedFile;
    private DocumentPageType type;

    public DocumentImage(String originalFile, DocumentPageType type) {
        this.originalFile = originalFile;
        this.croppedFile = originalFile;
        this.type = type;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public String getCroppedFile() {
        return croppedFile;
    }

    public void setCroppedFile(String croppedFile) {
        this.croppedFile = croppedFile;
    }

    public DocumentPageType getType() {
        return type;
    }

    public void setType(DocumentPageType type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalFile);
        dest.writeString(this.croppedFile);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected DocumentImage(Parcel in) {
        this.originalFile = in.readString();
        this.croppedFile = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : DocumentPageType.values()[tmpType];
    }

    public static final Creator<DocumentImage> CREATOR = new Creator<DocumentImage>() {
        @Override
        public DocumentImage createFromParcel(Parcel source) {
            return new DocumentImage(source);
        }

        @Override
        public DocumentImage[] newArray(int size) {
            return new DocumentImage[size];
        }
    };
}
