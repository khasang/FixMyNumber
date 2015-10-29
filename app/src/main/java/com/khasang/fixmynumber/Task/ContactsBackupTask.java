package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.format.DateFormat;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.Model.DBHelper;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class ContactsBackupTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    ArrayList<ContactItem> contactItems;
    private String myTable;
    private static final String PHONE = "PHONE";
    private static final String PHONE_ID = "PHONE_ID";
    private String phone;
    private String phoneId;
    public static final String dateFormat = "ddMMyyyyhhmmss";

    public ContactsBackupTask(Activity activity, ArrayList<ContactItem> contactItems) {
        this.activity = activity;
        this.contactItems = contactItems;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        myTable = "contacts" + (String) DateFormat.format(dateFormat, System.currentTimeMillis());
        db.execSQL("CREATE TABLE " + myTable + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + PHONE + " TEXT, " + PHONE_ID + " TEXT);");

        for (int i = 0; i < contactItems.size(); i++) {
            if (contactItems.get(i).isChecked()) {
                phone = contactItems.get(i).getNumberOriginal();
                phoneId = contactItems.get(i).getNumberOriginalId();
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.PHONE, phone);
                cv.put(DBHelper.PHONE_ID, phoneId);
                long id = db.insert(myTable, null, cv);
            }
        }
        dbHelper.close();
        return null;
    }
}
