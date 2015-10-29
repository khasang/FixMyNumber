package com.khasang.fixmynumber.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Task.DeleteReserveContactsTask;
import com.khasang.fixmynumber.Task.GetReserveContactsTask;
import com.khasang.fixmynumber.Task.LoadReserveContactsTask;

import java.util.ArrayList;

public class RestoreContactsActivity extends AppCompatActivity implements SavedContactsAdapter.SavedContactsItemClickListener {
    ArrayList<String> savedContactsList;
    String selectedTable;
    private RecyclerView recyclerViewSavedContacts;
    private AlertDialog dialogDelete;
    private AlertDialog dialogLoad;

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
        setTitle(R.string.restore_toolbar);
    }

    private void getDummySavedContacts() {
        savedContactsList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            savedContactsList.add(getString(R.string.contacts) + i);
        }
    }

    private void setUpRecyclerView() {
        recyclerViewSavedContacts = (RecyclerView) findViewById(R.id.recyclerViewSavedContacts);
        SavedContactsAdapter savedContactsAdapter = new SavedContactsAdapter(savedContactsList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSavedContacts.setAdapter(savedContactsAdapter);
        recyclerViewSavedContacts.setLayoutManager(layoutManager);
    }

    private void setUpButtons() {
        Button buttonLoad = (Button) findViewById(R.id.buttonLoad);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
        setUpDialogs();

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTable != null) {
                    dialogLoad.show();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTable != null) {
                    dialogDelete.show();
                    recyclerViewSavedContacts.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpDialogs() {
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builderDelete.setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Deleting " + selectedTable, Toast.LENGTH_SHORT).show();
                        new DeleteReserveContactsTask(RestoreContactsActivity.this, selectedTable).execute();
                        for (int i = 0; i < savedContactsList.size(); i++) {
                            if (savedContactsList.get(i).equals(selectedTable)) {
                                savedContactsList.remove(i);
                            }
                        }
                        recyclerViewSavedContacts.getAdapter().notifyDataSetChanged();
                        ((SavedContactsAdapter) recyclerViewSavedContacts.getAdapter()).resetSelection();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogDelete = builderDelete.create();

        AlertDialog.Builder builderLoad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builderLoad.setTitle(R.string.dialog_load_title)
                .setMessage(R.string.dialog_load_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Loading " + selectedTable, Toast.LENGTH_SHORT).show();
                        new LoadReserveContactsTask(RestoreContactsActivity.this, selectedTable).execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogLoad = builderLoad.create();
    }

    @Override
    public void onSavedContactsItemClick(String name) {
        selectedTable = name;
    }

    public void updateUI() {
        recyclerViewSavedContacts.getAdapter().notifyDataSetChanged();
    }
}
