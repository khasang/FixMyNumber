package com.khasang.fixmynumber.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactItem implements Parcelable{
    private String name;
    private String contactID;
    private String accountType;
    private String numberOriginal;
    private String numberNew;
    private boolean isChecked;
    private int group;

    public ContactItem() {
    }

    public ContactItem(String name, String contactID, String accountType, String numberOriginal, String numberNew, boolean isChecked) {
        this.name = name;
        this.numberOriginal = numberOriginal;
        this.contactID = contactID;
        this.numberNew = numberNew;
        this.isChecked = isChecked;
        this.accountType = accountType;
    }

    public ContactItem(String name, String contactID, String accountType, String numberOriginal, String numberNew, boolean isChecked, int group) {
        this.name = name;
        this.contactID = contactID;
        this.accountType = accountType;
        this.numberOriginal = numberOriginal;
        this.numberNew = numberNew;
        this.isChecked = isChecked;
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public ContactItem(Parcel parcel) {
        name = parcel.readString();
        numberOriginal = parcel.readString();
        contactID = parcel.readString();
        numberNew = parcel.readString();
        accountType = parcel.readString();
        int isCheckedInt = parcel.readInt();
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

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(numberOriginal);
        parcel.writeString(contactID);
        parcel.writeString(numberNew);
        parcel.writeString(accountType);
        int isCheckedInt;
        if (isChecked) {
            isCheckedInt = 1;
        } else {
            isCheckedInt = 0;
        }
        parcel.writeInt(isCheckedInt);
    }
}
