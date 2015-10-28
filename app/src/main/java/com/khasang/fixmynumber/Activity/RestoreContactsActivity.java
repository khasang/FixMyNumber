package com.khasang.fixmynumber.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.Model.DBHelper;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Task.DeleteReserveContactsTask;
import com.khasang.fixmynumber.Task.GetReserveContactsTask;
import com.khasang.fixmynumber.Task.LoadReserveContactsTask;

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
        new GetReserveContactsTask(this, savedContactsList).execute();
        setUpRecyclerView();
        setUpButtons();
    }

    private void getDummySavedContacts() {
        savedContactsList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
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
                if (selectedTable != null) {
                    Toast.makeText(getApplicationContext(), "Loading " + selectedTable, Toast.LENGTH_SHORT).show();
                    new LoadReserveContactsTask(RestoreContactsActivity.this, selectedTable).execute();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTable != null) {
                    Toast.makeText(getApplicationContext(), "Deleting " + selectedTable, Toast.LENGTH_SHORT).show();
                    new DeleteReserveContactsTask(RestoreContactsActivity.this, selectedTable).execute();
                }
            }
        });
    }

    @Override
    public void onSavedContactsItemClick(String name) {
        selectedTable = name;
    }

}
