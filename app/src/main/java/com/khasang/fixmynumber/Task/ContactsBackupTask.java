package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.format.DateFormat;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.Helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class ContactsBackupTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    ArrayList<ContactItem> contactList;
    private String myTable;
    private static final String NAME = "NAME";
    private static final String PHONE = "PHONE";
    private static final String PHONE_ID = "PHONE_ID";
    private static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    private String name;
    private String phone;
    private String phoneId;
    private String accountType;
    public static final String dateFormat = "ddMMyyyyhhmmss";

    public ContactsBackupTask(Activity activity, ArrayList<ContactItem> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        myTable = "contacts" + (String) DateFormat.format(dateFormat, System.currentTimeMillis());
        db.execSQL("CREATE TABLE " + myTable + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + PHONE + " TEXT, " + PHONE_ID + " TEXT, "
                + ACCOUNT_TYPE + " TEXT);");

        for (int i = 0; i < contactList.size(); i++) {
            name = contactList.get(i).getName();
            phone = contactList.get(i).getNumberOriginal();
            phoneId = contactList.get(i).getNumberOriginalId();
            accountType = contactList.get(i).getAccountType();
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.NAME, name);
            cv.put(DBHelper.PHONE, phone);
            cv.put(DBHelper.PHONE_ID, phoneId);
            cv.put(DBHelper.ACCOUNT_TYPE, accountType);
            long id = db.insert(myTable, null, cv);
        }
        dbHelper.close();
        return null;
    }
}
