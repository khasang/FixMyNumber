package com.khasang.fixmynumber.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactItem implements Parcelable{
    private String name;
    private String numberOriginal;
    private String numberOriginalId;
    private String numberNew;
    private boolean isChecked;
    private String accountType;
    private int isCheckedInt;

    public ContactItem() {
    }

    public ContactItem(String name, String numberOriginal, String numberOriginalId, String numberNew, boolean isChecked, String accountType) {
        this.name = name;
        this.numberOriginal = numberOriginal;
        this.numberOriginalId = numberOriginalId;
        this.numberNew = numberNew;
        this.isChecked = isChecked;
        this.accountType = accountType;
    }

    public ContactItem(Parcel parcel) {
        name = parcel.readString();
        numberOriginal = parcel.readString();
        numberOriginalId = parcel.readString();
        numberNew = parcel.readString();
        accountType = parcel.readString();
        isCheckedInt = parcel.readInt();
        if (isCheckedInt == 0) {
            isChecked = false;
        } else {
            isChecked = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOriginal() {
        return numberOriginal;
    }

    public void setNumberOriginal(String numberOriginal) {
        this.numberOriginal = numberOriginal;
    }

    public String getNumberOriginalId() {
        return numberOriginalId;
    }

    public void setNumberOriginalId(String numberOriginalId) {
        this.numberOriginalId = numberOriginalId;
    }

    public String getNumberNew() {
        return numberNew;
    }

    public void setNumberNew(String numberNew) {
        this.numberNew = numberNew;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel parcel) {
            return new ContactItem(parcel);
        }

        @Override
        public ContactItem[] newArray(int i) {
            return new ContactItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
