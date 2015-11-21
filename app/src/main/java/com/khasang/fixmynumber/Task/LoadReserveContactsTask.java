package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.khasang.fixmynumber.Model.DBHelper;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class LoadReserveContactsTask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private String selectedTable;
    private ArrayList<String> reserveNumbers = new ArrayList<>();
    private ArrayList<String> reserveNumberIds = new ArrayList<>();
    private String number;
    private String numberId;

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
            int numberIndex = c.getColumnIndex(DBHelper.PHONE);
            int numberIdIndex = c.getColumnIndex(DBHelper.PHONE_ID);
            do {
                number = c.getString(numberIndex);
                reserveNumbers.add(number);
                numberId = c.getString(numberIdIndex);
                reserveNumberIds.add(numberId);
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();

        for (int i = 0; i < reserveNumbers.size(); i++) {
            if (reserveNumbers.get(i) != null) {
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
