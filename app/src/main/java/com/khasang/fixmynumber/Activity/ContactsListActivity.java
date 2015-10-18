package com.khasang.fixmynumber.Activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.khasang.fixmynumber.Adapter.ContactsListAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;
import java.util.Random;

public class ContactsListActivity extends AppCompatActivity {
    ArrayList<ContactItem> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

//        createDummyContacts();
        createMoreDummyContacts();
//        contactsList = new ArrayList<>();
//        new ContactsLoader(contactsList).execute();
        setUpRecyclerView();
    }

    private void createMoreDummyContacts() {
        contactsList = new ArrayList<ContactItem>();
        String[] namesArray = {"Alice","Bob","Clover","Dennis","Fred","George","Harold"};
        String[] prefixArray = {"+7","8"};
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            int nameID = random.nextInt(namesArray.length);
            int prefixID = random.nextInt(prefixArray.length);
            String generatedName = namesArray[nameID];
            String generatedNumber = prefixArray[prefixID] + "800555-" + i;
            ContactItem newItem = new ContactItem(generatedName, generatedNumber, null, false);
            contactsList.add(newItem);
        }
    }

    private void createDummyContacts() {
        contactsList = new ArrayList<ContactItem>();
        for (int i = 0; i < 8; i++) {
            ContactItem newItem = new ContactItem("qwerty", "12345", null, false);
            contactsList.add(newItem);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerViewContacts = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        ContactsListAdapter adapter = new ContactsListAdapter(contactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewContacts.setAdapter(adapter);
        recyclerViewContacts.setLayoutManager(layoutManager);
    }

    public void buttonCheck(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);
        if (contactsList.get(1).isChecked()) {
            textView.setText("2nd contact is checked");
        } else {
            textView.setText("2nd contact is NOT checked");
        }
    }

    class ContactsLoader extends AsyncTask<Void, Void, Void> {

        ArrayList<ContactItem> contactItems;

        public ContactsLoader(ArrayList<ContactItem> contactItems) {
            this.contactItems = contactItems;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor numbersCursor = getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (numbersCursor.moveToNext()) {
                String number = numbersCursor.getString(
                        numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = numbersCursor.getString(
                        numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String id = numbersCursor.getString(
                        numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                ContactItem contactItem = new ContactItem(name, number, null, false);
                contactItems.add(contactItem);
            }
            return null;
        }

    }
}
