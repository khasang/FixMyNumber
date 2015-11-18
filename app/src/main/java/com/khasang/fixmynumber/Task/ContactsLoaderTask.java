package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;

import com.khasang.fixmynumber.Activity.FragmentActivity;
import com.khasang.fixmynumber.Helper.LoadingDialogCreator;
import com.khasang.fixmynumber.Model.ContactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Raenar on 28.10.2015.
 */
public class ContactsLoaderTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    ArrayList<ContactItem> contactsList;
    ArrayList<ContactItem> contactsListToShow;
    AlertDialog dialog;

    public ContactsLoaderTask(Activity activity, ArrayList<ContactItem> contactsList, ArrayList<ContactItem> contactsListToShow) {
        this.activity = activity;
        this.contactsList = contactsList;
        this.contactsListToShow = contactsListToShow;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LoadingDialogCreator loadingDialogCreator = new LoadingDialogCreator(activity);
        dialog = loadingDialogCreator.createLoadingDialog();
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<String> usedNumbersList = new ArrayList<>();

        Cursor numbersCursor = activity.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (numbersCursor.moveToNext()) {
            String number = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String id = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            String rawID = numbersCursor.getString(numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
//            String accountType = numbersCursor.getString(
//                    numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET));
            String accountType = null;
            Cursor rawCursor = activity.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,null,null,null,null);
            while (rawCursor.moveToNext()) {
                String thisRawID = rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts._ID));
                if (thisRawID.equals(rawID)) {
                    accountType = rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                    break;
                }
            }
            rawCursor.close();
            if (number != null) {
                if ((accountType.equals("USIM Account")) || (accountType.equals("SIM Account"))) {
                    number = Util.onlyDigits(number);
                }
                ContactItem contactItem = new ContactItem(name, number, id, null, false, accountType);
                contactsList.add(contactItem);
                if (!usedNumbersList.contains(number)) {
                    contactsListToShow.add(contactItem);
                    usedNumbersList.add(number);
                }
            }
        }
        numbersCursor.close();
        Collections.sort(contactsListToShow, new Comparator<ContactItem>() {
            public int compare(ContactItem o1, ContactItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((FragmentActivity) activity).updateUI();
    }
}
