package com.khasang.fixmynumber.Activity;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
//        createMoreDummyContacts();
        contactsList = new ArrayList<>();
        new ContactsLoader(contactsList).execute();
        setUpRecyclerView();
    }

    public void swapPrefix87(View view) {
        swapRrefix("8", "+7");
        new ContactsSaver(contactsList).execute();
    }

    public void swapPrefix78(View view) {
        swapRrefix("+7", "8");
        new ContactsSaver(contactsList).execute();
    }

    private void swapRrefix(String s1, String s2) {
        for (ContactItem contactItem : contactsList) {
            if (contactItem.isChecked()) {
                if (contactItem.getNumberOriginal().substring(0, s1.length()).equals(s1)) {
                    contactItem.setNumberNew(s2 + contactItem.getNumberOriginal().substring(s1.length()));
                } else {
                    contactItem.setNumberNew(contactItem.getNumberOriginal());
                }
            } else {
                contactItem.setNumberNew(contactItem.getNumberOriginal());
            }
        }
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
        RecyclerView RecyclerViewContacts = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        ContactsListAdapter adapter = new ContactsListAdapter(contactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerViewContacts.setAdapter(adapter);
        RecyclerViewContacts.setLayoutManager(layoutManager);
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
                if (number != null) {
                    ContactItem contactItem = new ContactItem(name, number, null, false);
                    contactItems.add(contactItem);
                }
            }
            numbersCursor.close();
            return null;
        }

    }

    class ContactsSaver extends AsyncTask<Void, Void, Void> {

        ArrayList<ContactItem> contactItems;

        public ContactsSaver(ArrayList<ContactItem> contactItems) {
            this.contactItems = contactItems;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < contactItems.size(); i++) {
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                                new String[]{contactItems.get(i).getNumberOriginal()})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactItems.get(i).getNumberNew())
                        .build());
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            }
            return null;
        }
    }
}
