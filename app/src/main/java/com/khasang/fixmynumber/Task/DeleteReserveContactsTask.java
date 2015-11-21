package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.khasang.fixmynumber.Model.DBHelper;

/**
 * Created by Raenar on 28.10.2015.
 */
public class DeleteReserveContactsTask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private String selectedTable;

    public DeleteReserveContactsTask(Activity activity, String selectedTable) {
        this.activity = activity;
        this.selectedTable = selectedTable;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.dropTable(db, selectedTable);
        return null;
    }
}
