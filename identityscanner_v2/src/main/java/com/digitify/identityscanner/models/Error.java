package com.digitify.identityscanner.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Error implements Parcelable {
    private String message;
    private int code;

    public Error(String message) {
        this.message = message;
    }

    public Error(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.code);
    }

    protected Error(Parcel in) {
        this.message = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<Error> CREATOR = new Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel source) {
            return new Error(source);
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[size];
        }
    };
}
