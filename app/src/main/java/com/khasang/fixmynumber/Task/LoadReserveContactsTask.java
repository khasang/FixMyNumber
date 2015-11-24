package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.khasang.fixmynumber.Helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class LoadReserveContactsTask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private String selectedTable;
    private ArrayList<String> reserveNumberNames = new ArrayList<>();
    private ArrayList<String> reserveNumbers = new ArrayList<>();
    private ArrayList<String> reserveNumberIds = new ArrayList<>();
    private ArrayList<String> reserveNumberAccountTypes = new ArrayList<>();
    private String name;
    private String number;
    private String numberId;
    private String numberAccountType;

    public LoadReserveContactsTask(Activity activity, String selectedTable) {
        this.activity = activity;
        this.selectedTable = selectedTable;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(selectedTable, null, null, null, null, null, null);
        if (c.moveToNext()) {
            int numberNameIndex = c.getColumnIndex(DBHelper.NAME);
            int numberIndex = c.getColumnIndex(DBHelper.PHONE);
            int numberIdIndex = c.getColumnIndex(DBHelper.PHONE_ID);
            int numberAccountTypeIndex = c.getColumnIndex(DBHelper.ACCOUNT_TYPE);
            do {
                name = c.getString(numberNameIndex);
                reserveNumberNames.add(name);
                number = c.getString(numberIndex);
                reserveNumbers.add(number);
                numberId = c.getString(numberIdIndex);
                reserveNumberIds.add(numberId);
                numberAccountType = c.getString(numberAccountTypeIndex);
                reserveNumberAccountTypes.add(numberAccountType);
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();

        for (int i = 0; i < reserveNumbers.size(); i++) {
            if (reserveNumbers.get(i) != null) {
                if (reserveNumberAccountTypes.get(i).equals("sim")) {
                    String presentNumber = null;
                    Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String presentName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            if (presentName.equals(reserveNumberNames.get(i))) {
                                presentNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                presentNumber = Util.onlyDigits(presentNumber);
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    Uri uri = Uri.parse("content://icc/adn");
                    ContentValues cv = new ContentValues();
                    cv.put("tag", reserveNumberNames.get(i));
                    cv.put("number", presentNumber);
                    cv.put("newTag", reserveNumberNames.get(i));
                    cv.put("newNumber", Util.onlyDigits(reserveNumbers.get(i)));
                    activity.getContentResolver().update(uri, cv, null, null);
                }
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.CommonDataKinds.Phone._ID + "=?",
                                new String[]{reserveNumberIds.get(i)})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, reserveNumbers.get(i))
                        .build());
                try {
                    activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            }
        }
        return null;
    }
}
