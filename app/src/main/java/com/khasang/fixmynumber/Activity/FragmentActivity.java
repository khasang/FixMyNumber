package com.khasang.fixmynumber.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Task.ContactsBackupTask;
import com.khasang.fixmynumber.Task.ContactsLoaderTask;
import com.khasang.fixmynumber.Task.ContactsSaverTask;

import java.util.ArrayList;
import java.util.Random;

public class FragmentActivity extends AppCompatActivity implements StepFragment.Fragment1ViewsCreateListener, StepFragment.Fragment2ViewsCreateListener, StepFragment.Fragment3ViewsCreateListener {
    CustomViewPager pager;
    ArrayList<ContactItem> contactsList;
    ArrayList<ContactItem> contactsListToShow;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewToChange;
    private View radioButtonSelected;
    private EditText editTextS1;
    private EditText editTextS2;
    private boolean areAllContactsSelected;
    private AlertDialog dialogConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsList = new ArrayList<ContactItem>();
        contactsListToShow = new ArrayList<ContactItem>();
        new ContactsLoaderTask(this, contactsList, contactsListToShow).execute();
        areAllContactsSelected = false;
        setUpPager();
//        createMoreDummyContacts();
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
            ContactItem newItem = new ContactItem(generatedName, generatedNumber, "" + i, null, false);
            contactsList.add(newItem);
        }
    }

    private void setUpPager() {
        setUpDialogs();
        pager = (CustomViewPager) findViewById(R.id.pager);
//        pager.setOffscreenPageLimit(0);
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
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builderDelete.setTitle(R.string.dialog_confirm_title)
                .setMessage(R.string.dialog_confirm_message)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Numbers are changed. See debug log (Tag 'ContactsSaverTask')",
                                Toast.LENGTH_LONG).show();
                        new ContactsBackupTask(FragmentActivity.this, contactsList).execute();
                        new ContactsSaverTask(FragmentActivity.this, contactsListToShow).execute();
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogConfirm = builderDelete.create();
    }

    private void updateButtons(Button backButton, Button nextButton) {
        int page = pager.getCurrentItem();
        if (page == 0) {
            backButton.setText(R.string.button_cancel);
        } else {
            backButton.setText(R.string.button_back);
        }
        if (page == 2) {
            changeNumbers();
            nextButton.setText(R.string.button_finish);
            recyclerViewToChange.getAdapter().notifyDataSetChanged();
//            next.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        } else {
            nextButton.setText(R.string.button_next);
//            next.setBackgroundColor(ContextCompat.getColor(this, android.support.v7.appcompat.R.color.button_material_light));;
        }
    }

    private void updateToolBar() {
        int page = pager.getCurrentItem();
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

    private void changeNumbers() {
        if (radioButtonSelected != null) {
            switch (radioButtonSelected.getId()) {
                case R.id.radioButtonSwap78:
                    swapPrefix("+7", "8");
                    break;
                case R.id.radioButtonSwap87:
                    swapPrefix("8", "+7");
                    break;
                case R.id.radioButtonSwap700:
                    swapPrefix("+7", "00");
                    break;
                case R.id.radioButtonSwap80:
                    swapPrefix("8", "0");
                    break;
                case R.id.radioButtonSwapCustom:
                    String s1 = editTextS1.getText().toString();
                    String s2 = editTextS2.getText().toString();
                    swapPrefix(s1, s2);
                    break;
            }
        }
    }

    private void swapPrefix(String s1, String s2) {
        for (ContactItem contactItem : contactsList) {
            if ((s1 != null) && (contactItem.isChecked())) {
                if (contactItem.getNumberOriginal().substring(0, s1.length()).equals(s1)) {
                    contactItem.setNumberNew(s2 + contactItem.getNumberOriginal().substring(s1.length()));
                }
            } else {
                contactItem.setNumberNew(contactItem.getNumberOriginal());
            }
        }
    }


    @Override
    public void onFragment1ViewsCreated(ViewGroup v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewContacts);

        CheckBox checkBoxSelectAll = (CheckBox) v.findViewById(R.id.checkBoxSelectAll);
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
    public void onFragment2ViewsCreated(View v, EditText ed1, EditText ed2) {
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
            StepFragment stepFragment = new StepFragment();
            stepFragment.setPageNumber(position);
            stepFragment.setContactsList(contactsListToShow);
            return stepFragment;
        }

        @Override
        public int getCount() {
            return FRAGMENTS_COUNT;
        }
    }

    public void updateUI() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}
