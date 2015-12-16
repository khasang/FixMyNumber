package com.khasang.fixmynumber.Activity;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.Fragment.RestoreListFragment;
import com.khasang.fixmynumber.Helper.DialogCreator;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Service.IntentHandler;

import java.util.ArrayList;

public class RestoreContactsActivity extends BaseServiceActivity implements SavedContactsAdapter.SavedContactsItemClickListener {
    ArrayList<String> savedContactsList;
    String selectedTable;
    private RecyclerView recyclerViewSavedContacts;
    private AlertDialog dialogDelete;
    private AlertDialog dialogLoad;
    private AlertDialog progressDialog;
    private RestoreListFragment fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_contacts);

        fragment1 = new RestoreListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, fragment1);
        transaction.commit();

        selectedTable = null;
//        savedContactsList = new ArrayList<>();
////        getDummySavedContacts();
        progressDialog = DialogCreator.createDialog(this, DialogCreator.LOADING_DIALOG);
        progressDialog.show();
        getServiceHelper().getBackupList();
//        setUpButtons();
        setTitle(R.string.restore_toolbar);
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        String action = requestIntent.getAction();
        switch (action) {
            case IntentHandler.ACTION_GET_BACKUP:
                savedContactsList = resultData.getStringArrayList(IntentHandler.BACKUP_TABLES_LIST_KEY);
                progressDialog.dismiss();
//                setUpRecyclerView();
                break;
            case IntentHandler.ACTION_LOAD_BACKUP:
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.restore_contacts_loaded, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case IntentHandler.ACTION_DELETE_BACKUP:
                break;
        }
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
                        getServiceHelper().deleteBackup(selectedTable);
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
                        progressDialog.show();
                        getServiceHelper().loadBackup(selectedTable);
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
