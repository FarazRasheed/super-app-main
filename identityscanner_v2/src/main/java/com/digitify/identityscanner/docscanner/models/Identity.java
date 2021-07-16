package com.digitify.identityscanner.docscanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.digitify.identityscanner.core.arch.Gender;

import java.util.Date;

import co.yap.yapcore.helpers.DateUtils;

public class Identity implements Parcelable {

    public static final Creator<Identity> CREATOR = new Creator<Identity>() {
        @Override
        public Identity createFromParcel(Parcel source) {
            return new Identity(source);
        }

        @Override
        public Identity[] newArray(int size) {
            return new Identity[size];
        }
    };
    /**
     * Document number, e.g. passport number.
     */
    public String documentNumber;
    /**
     * Document number, e.g. passport number.
     */
    public String citizenNumber;
    /**
     * Date of birth.
     */
    public Date dateOfBirth;

    public boolean isDateOfBirthValid() {

        return dateOfBirthValid = dateOfBirth != null && DateUtils.INSTANCE.getAge(dateOfBirth) >= 18;
    }

    public boolean isExpiryDateValid() {
        if (expirationDate == null) {
            return expiryDateValid = false;
        }
        return !(expiryDateValid = DateUtils.INSTANCE.isDatePassed(expirationDate));
    }

    private boolean dateOfBirthValid;

    /**
     * expiration date of passport
     */
    public Date expirationDate;
    private boolean expiryDateValid;
    private Gender gender;
    private String givenName = "";
    private String sirName = "";
    private String nationality = "";
    private String issuingCountry = "";
    public String isoCountryCode2Digit = "";
    public String isoCountryCode3Digit = "";

    public Identity() {
    }

    protected Identity(Parcel in) {
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.givenName = in.readString();
        this.sirName = in.readString();
        this.nationality = in.readString();
        this.issuingCountry = in.readString();
        this.documentNumber = in.readString();
        this.citizenNumber = in.readString();
        this.isoCountryCode2Digit = in.readString();
        this.isoCountryCode3Digit = in.readString();
        this.dateOfBirth = (Date) in.readSerializable();
        this.expirationDate = (Date) in.readSerializable();
        expiryDateValid = in.readInt() == 1;
        dateOfBirthValid = in.readInt() == 1;
//        int tmpFormat = in.readInt();
//        this.format = tmpFormat == -1 ? null : MrzFormat.values()[tmpFormat];
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSirName() {
        return sirName;
    }

    public void setSirName(String sirName) {
        this.sirName = sirName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


//    public MrzFormat getFormat() {
//        return format;
//    }

//    public void setFormat(MrzFormat format) {
//        this.format = format;
//    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "gender=" + gender +
                ", givenName='" + givenName + '\'' +
                ", sirName='" + sirName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", issuingCountry='" + issuingCountry + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", citizenNumber='" + citizenNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", expirationDate=" + expirationDate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.givenName);
        dest.writeString(this.sirName);
        dest.writeString(this.nationality);
        dest.writeString(this.issuingCountry);
        dest.writeString(this.documentNumber);
        dest.writeString(this.citizenNumber);
        dest.writeString(this.isoCountryCode2Digit);
        dest.writeString(this.isoCountryCode3Digit);
        dest.writeSerializable(this.dateOfBirth);
        dest.writeSerializable(this.expirationDate);
        dest.writeInt(expiryDateValid ? 1 : 0);
        dest.writeInt(dateOfBirthValid ? 1 : 0);
//        dest.writeInt(this.format == null ? -1 : this.format.ordinal());
    }
}
