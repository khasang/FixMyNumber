package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.khasang.fixmynumber.Activity.RestoreContactsActivity;
import com.khasang.fixmynumber.Helper.DialogCreator;
import com.khasang.fixmynumber.Helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class GetReserveContactsTask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private ArrayList<String> savedContactsList;
    private String result;
    AlertDialog dialog;

    public GetReserveContactsTask(Activity activity, ArrayList<String> savedContactsList) {
        this.activity = activity;
        this.savedContactsList = savedContactsList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DialogCreator dialogCreator = new DialogCreator();
        dialog = dialogCreator.createDialog(activity, 0);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor c = db.query(DBHelper.SQLITE_SEQUENCE, null, null, null, null, null, null);
            if (c.moveToNext()) {
                int nameIndex = c.getColumnIndex("name");
                do {
                    result = c.getString(nameIndex);
                    savedContactsList.add(result);
                } while (c.moveToNext());
            }
            c.close();
            dbHelper.close();
        } catch (SQLiteException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((RestoreContactsActivity) activity).updateUI();
    }
}
