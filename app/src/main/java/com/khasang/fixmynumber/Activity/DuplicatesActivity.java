package com.khasang.fixmynumber.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.khasang.fixmynumber.Fragment.DuplicateFragment;
import com.khasang.fixmynumber.Fragment.StepFragment1;
import com.khasang.fixmynumber.Helper.DialogCreator;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Service.IntentHandler;

import java.util.ArrayList;

public class DuplicatesActivity extends BaseServiceActivity implements StepFragment1.Fragment1ContactClickListener {
    AlertDialog progressDialog;
    ArrayList<ContactItem> contactsList;
    private DuplicateFragment duplicateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicates);
        progressDialog = DialogCreator.createDialog(this, DialogCreator.LOADING_DIALOG);
        progressDialog.show();
        duplicateFragment = new DuplicateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, duplicateFragment).commit();
        getServiceHelper().loadContacts();
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        String action = requestIntent.getAction();
        switch (action) {
            case IntentHandler.ACTION_LOAD:
//                contactsList = resultData.getParcelableArrayList(IntentHandler.LOAD_LIST_KEY);
                contactsList = new ArrayList<>();
                contactsList.add(new ContactItem("TestA1","1","testType","111","222",false,1));
                contactsList.add(new ContactItem("TestB1","1","testType","111","222",false,1));
                contactsList.add(new ContactItem("TestC1","1","testType","111","222",false,1));
                contactsList.add(new ContactItem("TestA2","1","testType","111","222",false,2));
                contactsList.add(new ContactItem("TestB2","1","testType","111","222",false,2));
                contactsList.add(new ContactItem("TestA3","1","testType","111","222",false,3));
                ArrayList<Integer> groupList = new ArrayList<>();
                groupList.add(1);
                groupList.add(2);
                groupList.add(3);
                duplicateFragment.setList(this, contactsList, groupList);
                progressDialog.dismiss();
                break;
        }

    }

    @Override
    public void onFragment1ContactClick() {

    }
}