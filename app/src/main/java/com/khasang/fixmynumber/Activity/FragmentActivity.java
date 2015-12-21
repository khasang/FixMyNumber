package com.khasang.fixmynumber.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.khasang.fixmynumber.Fragment.StepFragment1;
import com.khasang.fixmynumber.Fragment.StepFragment2;
import com.khasang.fixmynumber.Fragment.StepFragment3;
import com.khasang.fixmynumber.Helper.DialogCreator;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Service.IntentHandler;
import com.khasang.fixmynumber.View.CustomViewPager;

import java.util.ArrayList;
import java.util.Random;

public class FragmentActivity extends BaseServiceActivity implements StepFragment1.Fragment1ViewsCreateListener, StepFragment1.Fragment1ContactClickListener, StepFragment2.Fragment2ViewsUpdateListener, StepFragment3.Fragment3ViewsCreateListener {
    CustomViewPager pager;
    ArrayList<ContactItem> contactsList;
    ArrayList<ContactItem> contactsListToShow;
    ArrayList<ContactItem> contactsListChanged;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewToChange;
    private View radioButtonSelected;
    private EditText editTextS1;
    private EditText editTextS2;
    private boolean areAllContactsSelected;
    private AlertDialog dialogConfirm;
    private AlertDialog progressDialog;
    private AlertDialog backupDialog;
    private AlertDialog savingDialog;
    private CheckBox checkBoxSelectAll;
    public static final String FILE_NAME = "preferences";
    public static final String DATA_MAP_KEY = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactsList = new ArrayList<ContactItem>();
        contactsListToShow = new ArrayList<ContactItem>();
        contactsListChanged = new ArrayList<ContactItem>();
        progressDialog = DialogCreator.createDialog(this, DialogCreator.LOADING_DIALOG);
        backupDialog = DialogCreator.createDialog(this, DialogCreator.BACKUP_DIALOG);
        savingDialog = DialogCreator.createDialog(this, DialogCreator.SAVING_DIALOG);
        getServiceHelper().loadContacts();
        progressDialog.show();
        areAllContactsSelected = false;
        showInfoDialog();
    }

    private void showInfoDialog() {
        final SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        if (!sharedPreferences.contains(DATA_MAP_KEY)) {
            DialogCreator.createDialog(this, DialogCreator.INFO_DIALOG)
                    .show();
        }

    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        String action = requestIntent.getAction();
        switch (action) {
            case IntentHandler.ACTION_LOAD:
                contactsList = resultData.getParcelableArrayList(IntentHandler.LOAD_LIST_KEY);
                contactsListToShow = resultData.getParcelableArrayList(IntentHandler.LIST_TO_SHOW_KEY);
                setUpUI();
                progressDialog.dismiss();
                updateContactsList();
                break;

            case IntentHandler.ACTION_SAVE:
                savingDialog.dismiss();
                finish();
                break;

            case IntentHandler.ACTION_BACKUP:
                String backupName = getApplicationContext().getString(R.string.contacts) + " "
                        + resultData.getString(IntentHandler.BACKUP_TIME_KEY);
                String backupMessage = getString(R.string.backup_message) + "\n" + backupName;
                Toast.makeText(getApplicationContext(),
                        backupMessage,
                        Toast.LENGTH_LONG).show();
                backupDialog.dismiss();
                savingDialog.show();
                getServiceHelper().saveContacts(contactsListToShow);
//                finish();
                break;
        }

    }

    private void createMoreDummyContacts() {
        contactsList = new ArrayList<ContactItem>();
        String[] namesArray = {"Alice", "Bob", "Clover", "Dennis", "Fred", "George", "Harold"};
        String[] prefixArray = {"+7", "8"};
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            int nameID = random.nextInt(namesArray.length);
            int prefixID = random.nextInt(prefixArray.length);
            String generatedName = namesArray[nameID];
            String generatedNumber = prefixArray[prefixID] + "800555-" + i;
            ContactItem newItem = new ContactItem(generatedName, generatedNumber, "" + i, null, false, null);
            contactsList.add(newItem);
        }
    }

    private void setUpUI() {
        setUpDialogs();
        pager = (CustomViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        final Button backButton = (Button) findViewById(R.id.prev_button);
        final Button nextButton = (Button) findViewById(R.id.next_button);
        updateButtons(backButton, nextButton);
        updateToolBar();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = pager.getCurrentItem();
                if (page > 0) {
                    pager.setCurrentItem(page - 1);
                } else {
                    finish();
                }
                updateButtons(backButton, nextButton);
                updateToolBar();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = pager.getCurrentItem();
                if (page < pagerAdapter.getCount() - 1) {
                    pager.setCurrentItem(page + 1);
                } else {
                    dialogConfirm.show();
                }
                updateButtons(backButton, nextButton);
                updateToolBar();
            }
        });
    }

    private void setUpDialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.dialog_confirm_title)
                .setMessage(R.string.dialog_confirm_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backupDialog.show();
                        getServiceHelper().backupContacts(contactsList);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogConfirm = builder.create();
    }

    private void updateButtons(Button backButton, Button nextButton) {
        int page = pager.getCurrentItem();
        nextButton.setEnabled(true);
        switch (page) {
            case 0:
                backButton.setText(R.string.button_cancel);
                nextButton.setText(R.string.button_next);
                break;
            case 1:
                backButton.setText(R.string.button_back);
                nextButton.setText(R.string.button_next);
                break;
            case 2:
                backButton.setText(R.string.button_back);
                nextButton.setText(R.string.button_finish);
                changeNumbers();
                contactsListChanged.clear();
                for (ContactItem contactItem : contactsListToShow) {
                    if (contactItem.getNumberNew() != null) {
                        if ((!contactItem.getNumberNew().equals(contactItem.getNumberOriginal()))) {
                            contactsListChanged.add(contactItem);
                        }
                    }
                }
                if (contactsListChanged.size() == 0) {
                    nextButton.setEnabled(false);
                }
                recyclerViewToChange.getAdapter().notifyDataSetChanged();
//            next.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }
    }

    private void updateToolBar() {
        int page = pager.getCurrentItem();
        if (getSupportActionBar() != null) {
            switch (page) {
                case 0:
                    getSupportActionBar().setTitle(R.string.change_toolbar1);
                    break;
                case 1:
                    getSupportActionBar().setTitle(R.string.change_toolbar2);
                    break;
                case 2:
                    getSupportActionBar().setTitle(R.string.change_toolbar3);
                    break;
            }
        }
    }

    private void changeNumbers() {
        if (radioButtonSelected != null) {
            switch (radioButtonSelected.getId()) {
                case R.id.radioButtonSwap78:
                    swapPrefix("+7", "8");
                    break;
                case R.id.radioButtonSwap87:
                    swapPrefix("8", "+7");
                    break;
//                case R.id.radioButtonSwap700:
//                    swapPrefix("+7", "00");
//                    break;
//                case R.id.radioButtonSwap80:
//                    swapPrefix("8", "0");
//                    break;
                case R.id.radioButtonSwapCustom:
                    String s1 = editTextS1.getText().toString();
                    String s2 = editTextS2.getText().toString();
                    swapPrefix(s1, s2);
                    break;
            }
        }
    }

    private void swapPrefix(String s1, String s2) {
        for (ContactItem contactItem : contactsListToShow) {
            if ((s1 != null) && (contactItem.isChecked())) {
                if (contactItem.getNumberOriginal().substring(0, s1.length()).equals(s1)) {
                    contactItem.setNumberNew(s2 + contactItem.getNumberOriginal().substring(s1.length()));
                } else {
                    contactItem.setNumberNew(contactItem.getNumberOriginal());
                }
            } else {
                contactItem.setNumberNew(contactItem.getNumberOriginal());
            }
        }
    }


    @Override
    public void onFragment1ViewsCreated(ViewGroup v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewContacts);

        checkBoxSelectAll = (CheckBox) v.findViewById(R.id.checkBoxSelectAll);
        checkBoxSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areAllContactsSelected = !areAllContactsSelected;
                for (ContactItem contactItem : contactsList) {
                    contactItem.setIsChecked(areAllContactsSelected);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFragment1ContactClick() {
        areAllContactsSelected = true;
        for (ContactItem contactItem : contactsList) {
            if (!contactItem.isChecked()) {
                areAllContactsSelected = false;
            }
        }
        checkBoxSelectAll.setChecked(areAllContactsSelected);
    }

    @Override
    public void onFragment2ViewsUpdated(View v, EditText ed1, EditText ed2) {
        radioButtonSelected = v;
        editTextS1 = ed1;
        editTextS2 = ed2;
    }

    @Override
    public void onFragment3ViewsCreated(ViewGroup v) {
        recyclerViewToChange = (RecyclerView) v.findViewById(R.id.recyclerViewContactsToChange);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public static final int FRAGMENTS_COUNT = 3;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    StepFragment1 stepFragment1 = new StepFragment1();
                    stepFragment1.setContactsList(contactsListToShow);
                    return stepFragment1;
                case 1:
                    StepFragment2 stepFragment2 = new StepFragment2();
                    stepFragment2.setContactsList(contactsListToShow);
                    return stepFragment2;
                case 2:
                    StepFragment3 stepFragment3 = new StepFragment3();
                    stepFragment3.setContactsList(contactsListChanged);
                    return stepFragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return FRAGMENTS_COUNT;
        }
    }

    public void updateContactsList() {
        if (recyclerView != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

}
