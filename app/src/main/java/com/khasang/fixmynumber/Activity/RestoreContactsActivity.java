package com.khasang.fixmynumber.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.Fragment.OnButtonClickListener;
import com.khasang.fixmynumber.Fragment.RestoreFragment1;
import com.khasang.fixmynumber.Fragment.RestoreFragment2;
import com.khasang.fixmynumber.Helper.DialogCreator;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Service.IntentHandler;

import java.util.ArrayList;

public class RestoreContactsActivity extends BaseServiceActivity implements SavedContactsAdapter.SavedContactsItemClickListener,
        OnButtonClickListener {
    ArrayList<String> savedContactsList;
    String selectedTable;
    private AlertDialog dialogDelete;
    private AlertDialog dialogLoad;
    private AlertDialog progressDialog;
    private RestoreFragment1 fragment1;
    private RestoreFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_contacts);

        fragment1 = new RestoreFragment1();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, fragment1);
        transaction.commit();

        selectedTable = null;
//        savedContactsList = new ArrayList<>();
        progressDialog = DialogCreator.createDialog(this, DialogCreator.LOADING_DIALOG);
        progressDialog.show();
        getServiceHelper().getBackupList();
        setUpDialogs();
        setTitle(R.string.restore_toolbar);
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        String action = requestIntent.getAction();
        switch (action) {
            case IntentHandler.ACTION_GET_BACKUP:
                savedContactsList = resultData.getStringArrayList(IntentHandler.BACKUP_TABLES_LIST_KEY);
                progressDialog.dismiss();
                fragment1.setList(savedContactsList);
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

    @Override
    public void OnButtonClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDelete:
                if (selectedTable != null) {
                    dialogDelete.show();
                }
                break;
            case R.id.buttonLoad:
                if (selectedTable != null) {
                    fragment2 = new RestoreFragment2();
//                    fragment2.setList();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment2);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.buttonCancel:
                getSupportFragmentManager().popBackStack();
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonConfirm:
                dialogLoad.show();
                Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
                break;


        }
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
                        fragment1.updateList();
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

}
