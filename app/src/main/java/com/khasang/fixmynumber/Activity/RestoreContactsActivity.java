package com.khasang.fixmynumber.Activity;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.Model.DBHelper;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

public class RestoreContactsActivity extends AppCompatActivity implements SavedContactsAdapter.SavedContactsItemClickListener {
    ArrayList<String> savedContactsList;
    String selectedTable;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_contacts);

        selectedTable = null;
        savedContactsList = new ArrayList<>();
//        getDummySavedContacts();
        new TaskGetReserveContacts(savedContactsList).execute();
        setUpRecyclerView();
        setUpButtons();
    }

    private void getDummySavedContacts() {
        savedContactsList = new ArrayList<String>();
        for (int i = 0; i < 20 ; i++) {
            savedContactsList.add("Контакты " + i);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerViewSavedContacts = (RecyclerView) findViewById(R.id.recyclerViewSavedContacts);
        SavedContactsAdapter savedContactsAdapter = new SavedContactsAdapter(savedContactsList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSavedContacts.setAdapter(savedContactsAdapter);
        recyclerViewSavedContacts.setLayoutManager(layoutManager);
    }

    private void setUpButtons() {
        Button buttonLoad = (Button) findViewById(R.id.buttonLoad);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTable!=null){
                    Toast.makeText(getApplicationContext(), "Loading "+selectedTable, Toast.LENGTH_SHORT).show();
                    new TaskLoadReserveContacts(selectedTable).execute();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTable!=null){
                    Toast.makeText(getApplicationContext(), "Deleting "+selectedTable, Toast.LENGTH_SHORT).show();
                    new TaskDeleteReserveContacts(selectedTable).execute();
                }
            }
        });
    }

    @Override
    public void onSavedContactsItemClick(String name) {
        selectedTable = name;
    }

    class  TaskGetReserveContacts extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> savedContactsList;
        private String result;

        public TaskGetReserveContacts(ArrayList<String> savedContactsList) {
            this.savedContactsList = savedContactsList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper(RestoreContactsActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
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
            return null;
        }
    }

    class  TaskLoadReserveContacts extends AsyncTask<Void, Void, Void> {
        private String selectedTable;
        private ArrayList<String> reserveNumbers = new ArrayList<>();
        private ArrayList<String> reserveNumberIds = new ArrayList<>();
        private String number;
        private String numberId;

        public TaskLoadReserveContacts(String selectedTable) {
            this.selectedTable = selectedTable;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper(RestoreContactsActivity.this);
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
                if (reserveNumbers.get(i) != null){
                    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                    op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(ContactsContract.CommonDataKinds.Phone._ID + "=?",
                                    new String[]{reserveNumberIds.get(i)})
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, reserveNumbers.get(i))
                            .build());
                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                    } catch (Exception e) {
                        Log.e("Exception: ", e.getMessage());
                    }
                }
            }
            return null;
        }
    }

    class TaskDeleteReserveContacts extends AsyncTask<Void, Void, Void> {
        private String selectedTable;

        public TaskDeleteReserveContacts(String selectedTable) {
            this.selectedTable = selectedTable;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper(RestoreContactsActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.dropTable(db, selectedTable);
            return null;
        }
    }
}
