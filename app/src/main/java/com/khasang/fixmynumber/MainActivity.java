package com.khasang.fixmynumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactItem> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDummyContacts();
        setUpRecyclerView();
    }

    private void createDummyContacts() {
        contactList = new ArrayList<ContactItem>();
        for (int i = 0; i < 8; i++) {
            ContactItem newItem = new ContactItem("qwerty", "12345", null, false);
            contactList.add(newItem);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView RecyclerViewContacts = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        ContactsListAdapter adapter = new ContactsListAdapter(contactList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerViewContacts.setAdapter(adapter);
        RecyclerViewContacts.setLayoutManager(layoutManager);
    }

    public void buttonCheck(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);
        if (contactList.get(1).isChecked()) {
            textView.setText("2nd contact is checked");
        } else {
            textView.setText("2nd contact is NOT checked");
        }
    }
}
