package com.khasang.fixmynumber.Service;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.util.Log;

import com.khasang.fixmynumber.Model.ContactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Raenar on 01.12.2015.
 */
public class TestIntentHandler extends BaseIntentHandler {
    public static final String ACTION_LOAD = "action load";
    public static final String LOAD_ID = "load id";
    public static final String PROGRESS_KEY_LOAD = "progress load";
    public static final String RESULT_KEY_LOAD_LIST = "result load list";
    public static final String RESULT_KEY_LOAD_LIST_TO_SHOW = "result load list to show";

    public static final String ACTION_SAVE = "action save";
    public static final String SAVE_ID = "save id";
    public static final String PROGRESS_KEY_SAVE = "progress save";
    public static final String RESULT_KEY_SAVE_LIST = "result save list";
    public static final String SAVED_LIST_NAME = "saved list name";

    public static final int PROGRESS_CODE = 0;
    public static final int RESULT_SUCCESS_CODE = 1;
    public static final int RESULT_FAILURE_CODE = 2;

    ArrayList<ContactItem> contactsList;
    ArrayList<ContactItem> contactsListToShow;

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_LOAD: {
                contactsList = new ArrayList<>();
                contactsListToShow = new ArrayList<>();
                getContacts(context);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(RESULT_KEY_LOAD_LIST, contactsList);
                bundle.putParcelableArrayList(RESULT_KEY_LOAD_LIST_TO_SHOW, contactsListToShow);
                callback.send(RESULT_SUCCESS_CODE, bundle);
            }
            break;
            case ACTION_SAVE: {
                contactsList = new ArrayList<>();
                contactsList = intent.getParcelableArrayListExtra(SAVED_LIST_NAME);
                saveContacts(context, contactsList);
            }
            break;
        }

    }

    private void saveContacts(Context context, ArrayList<ContactItem> contactList) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getNumberNew() != null && contactList.get(i).isChecked()) {
//                if (contactList.get(i).getAccountType().equals("sim")) {
//                    Uri uri = Uri.parse("content://icc/adn");
//                    ContentValues cv = new ContentValues();
//                    cv.put("tag", contactList.get(i).getName());
//                    String number = Util.onlyDigits(contactList.get(i).getNumberOriginal());
//                    cv.put("number", number);
//                    cv.put("newTag", contactList.get(i).getName());
//                    String numberNew = Util.onlyDigits(contactList.get(i).getNumberNew());
//                    cv.put("newNumber", numberNew);
//                    context.getContentResolver().update(uri, cv, null, null);
//
//                    Log.d("SIM", "Saved Name=" + contactList.get(i).getName() + ";number ="
//                            + number
//                            + ";new number =" + numberNew);
//                }
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                                new String[]{contactList.get(i).getNumberOriginal()})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactList.get(i).getNumberNew())
                        .build());
                try {
                    context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                    Log.d("ContactsSaverTask", "changed " + contactList.get(i).getName()
                            + " " + contactList.get(i).getNumberOriginal()
                            + " => to " + contactList.get(i).getNumberNew());
                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            } else {
                Log.d("ContactsSaverTask", "Unchanged: " + contactList.get(i).getName());
            }
        }
    }

    void getContacts(Context context) {
        ArrayList<String> usedNumbersList = new ArrayList<>();

        Cursor numbersCursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (numbersCursor.moveToNext()) {
            String number = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String id = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            String rawID = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
//            String accountType = numbersCursor.getString(
//                    numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET));
            String accountType = null;
            Cursor rawCursor = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
            while (rawCursor.moveToNext()) {
                String thisRawID = rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts._ID));
                if (thisRawID.equals(rawID)) {
                    accountType = rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                    break;
                }
            }
            rawCursor.close();
            if (number != null && number.length() > 5) {
//                number = Util.onlyDigits(number);
                if (accountType != null) {
                    for (int i = 0; i < accountType.length() - 2; i++) {
                        if (accountType.charAt(i) == 's' || accountType.charAt(i) == 'S') {
                            if (accountType.charAt(i + 1) == 'i' || accountType.charAt(i + 1) == 'I') {
                                if (accountType.charAt(i + 2) == 'm' || accountType.charAt(i + 2) == 'M') {
                                    accountType = "sim";
//                                number = Util.onlyDigits(number);
                                }
                            }
                        }
                    }
                }
                ContactItem contactItem = new ContactItem(name, number, id, null, false, accountType);
                contactsList.add(contactItem);
                if (!usedNumbersList.contains(number)) {
                    contactsListToShow.add(contactItem);
                    usedNumbersList.add(number);
                }
//                else if (accountType.equals("sim")) {
//                    for (ContactItem contact : contactsListToShow) {
//                        if (contact.getNumberOriginal().equals(number)) {
//                            contact.setAccountType("sim");
//                        }
//                    }
//                }
            }
        }
        numbersCursor.close();
        Collections.sort(contactsListToShow, new Comparator<ContactItem>() {
            public int compare(ContactItem o1, ContactItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
}
