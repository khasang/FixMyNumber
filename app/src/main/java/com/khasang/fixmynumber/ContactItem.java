package com.khasang.fixmynumber;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactItem {
    private String name;
    private String numberOriginal;
    private String numberNew;
    private boolean isChecked;

    public ContactItem() {
    }

    public ContactItem(String name, String numberOriginal, String numberNew, boolean isChecked) {
        this.name = name;
        this.numberOriginal = numberOriginal;
        this.numberNew = numberNew;
        this.isChecked = isChecked;
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
}
