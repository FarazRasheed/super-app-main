package com.digitify.identityscanner.docscanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.digitify.identityscanner.core.arch.ScanLimitReachedException;
import com.digitify.identityscanner.docscanner.enums.DocumentType;

import java.util.ArrayList;

public class Document implements Parcelable {

    private DocumentType type;
    private ArrayList<DocumentImage> files;

    public Document() {
        this(DocumentType.UNKNOWN);
    }

    public Document(DocumentType type) {
        this.type = type;
        files = new ArrayList<>();
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        if (type == null) type = DocumentType.UNKNOWN;
        this.type = type;
    }

    public ArrayList<DocumentImage> getFiles() {
        return files;
    }

    public void addFile(DocumentImage file) throws ScanLimitReachedException {
        DocumentType t = getType();
        if (this.files.size() >= t.getNumberOfPages()) {
            throw new ScanLimitReachedException();
        }
        this.files.add(file);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeList(this.files);
    }

    protected Document(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : DocumentType.values()[tmpType];
        this.files = new ArrayList<>();
        in.readList(this.files, DocumentImage.class.getClassLoader());
    }

    public static final Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel source) {
            return new Document(source);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
}
