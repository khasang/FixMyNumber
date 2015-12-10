package com.khasang.fixmynumber.Service;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;

import com.khasang.fixmynumber.Helper.DBHelper;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.Task.Util;

import java.util.ArrayList;

/**
 * Created by Raenar on 01.12.2015.
 */
public class TestIntentHandler extends BaseIntentHandler {
    public static final int PROGRESS_CODE = 0;
    public static final int RESULT_SUCCESS_CODE = 1;
    public static final int RESULT_FAILURE_CODE = 2;

    public static final String ACTION_LOAD = "action load";
    public static final String LOAD_LIST_KEY = "load list";
    public static final String LIST_TO_SHOW_KEY = "load list to show";

    public static final String ACTION_SAVE = "action save";
    public static final String SAVED_LIST_KEY = "saved list";

    public static final String ACTION_BACKUP = "action backup";
    public static final String BACKUP_LIST_KEY = "backup list key";
    public static final String BACKUP_TIME_KEY = "backup time key";

    public static final String ACTION_GET_BACKUP = "ACTION_GET_BACKUP";
    public static final String BACKUP_TABLES_LIST_KEY = "BACKUP_TABLES_LIST_KEY";

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
                bundle.putParcelableArrayList(LOAD_LIST_KEY, contactsList);
                bundle.putParcelableArrayList(LIST_TO_SHOW_KEY, contactsListToShow);
                callback.send(RESULT_SUCCESS_CODE, bundle);
            }
            break;
            case ACTION_SAVE: {
                contactsList = new ArrayList<>();
                contactsList = intent.getParcelableArrayListExtra(SAVED_LIST_KEY);
                saveContacts(context, contactsList);

                Bundle bundle = new Bundle();
                callback.send(RESULT_SUCCESS_CODE, bundle);
            }
            break;
            case ACTION_BACKUP: {
                contactsList = intent.getParcelableArrayListExtra(BACKUP_LIST_KEY);
                Bundle bundle = new Bundle();
                String backupTime = createContactsBackup(context, contactsList);

                bundle.putString(BACKUP_TIME_KEY, backupTime);
                callback.send(RESULT_SUCCESS_CODE, bundle);
            }
            case ACTION_GET_BACKUP: {
                ArrayList<String> backupList = getBackupContacts(context);
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(BACKUP_TABLES_LIST_KEY, backupList);
                callback.send(RESULT_SUCCESS_CODE, bundle);
            }
        }

    }

    void getContacts(Context context) {
        ArrayList<String> usedNumbersList = new ArrayList<>();

        Cursor numbersCursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
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
                } else if (accountType.equals("sim")) {
                    for (ContactItem contact : contactsListToShow) {
                        if (contact.getNumberOriginal().equals(number)) {
                            contact.setAccountType("sim");
                        }
                    }
                }
            }
        }
        numbersCursor.close();
    }

    private void saveContacts(Context context, ArrayList<ContactItem> contactList) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getNumberNew() != null && contactList.get(i).isChecked()) {
                if (contactList.get(i).getAccountType() != null) {
                    if (contactList.get(i).getAccountType().equals("sim")) {
                        Uri uri = Uri.parse("content://icc/adn");
                        ContentValues cv = new ContentValues();
                        cv.put("tag", contactList.get(i).getName());
                        String number = Util.onlyDigits(contactList.get(i).getNumberOriginal());
                        cv.put("number", number);
                        cv.put("newTag", contactList.get(i).getName());
                        String numberNew = Util.onlyDigits(contactList.get(i).getNumberNew());
                        cv.put("newNumber", numberNew);
                        context.getContentResolver().update(uri, cv, null, null);

                        Log.d("SIM", "Saved Name=" + contactList.get(i).getName() + ";number ="
                                + number
                                + ";new number =" + numberNew);
                    }
                }
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

    private String createContactsBackup(Context context, ArrayList<ContactItem> contactList) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long currentTime = System.currentTimeMillis();
        String backupTimeShort = (String) DateFormat.format(DBHelper.dateFormatShort, currentTime);
        String backupTimeFull = (String) DateFormat.format(DBHelper.dateFormatFull, currentTime);

        String newTableName = "contacts" + backupTimeShort;
        db.execSQL("CREATE TABLE " + newTableName + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + DBHelper.NAME + " TEXT, " + DBHelper.PHONE + " TEXT, " + DBHelper.PHONE_ID + " TEXT, "
                + DBHelper.ACCOUNT_TYPE + " TEXT);");

        for (int i = 0; i < contactList.size(); i++) {
            String name = contactList.get(i).getName();
            String phone = contactList.get(i).getNumberOriginal();
            String phoneId = contactList.get(i).getNumberOriginalId();
            String accountType = contactList.get(i).getAccountType();

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.NAME, name);
            cv.put(DBHelper.PHONE, phone);
            cv.put(DBHelper.PHONE_ID, phoneId);
            cv.put(DBHelper.ACCOUNT_TYPE, accountType);
            long id = db.insert(newTableName, null, cv);
        }
        dbHelper.close();
        return backupTimeFull;
    }

    private ArrayList<String> getBackupContacts(Context context) {
        ArrayList<String> backupList = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor c = db.query(DBHelper.SQLITE_SEQUENCE, null, null, null, null, null, null);
            if (c.moveToNext()) {
                int nameIndex = c.getColumnIndex("name");
                do {
                    String result = c.getString(nameIndex);
                    backupList.add(result);
                } while (c.moveToNext());
            }
            c.close();
            dbHelper.close();
        } catch (SQLiteException e) {

        }
        return backupList;
    }
}
