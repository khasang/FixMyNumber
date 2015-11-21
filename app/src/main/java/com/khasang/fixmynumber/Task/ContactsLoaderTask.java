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
            String number = numbersCursor.getString(
                    numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = numbersCursor.getString(
                    numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String id = numbersCursor.getString(
                    numbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            if (number != null) {
                ContactItem contactItem = new ContactItem(name, number, id, null, false);
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
