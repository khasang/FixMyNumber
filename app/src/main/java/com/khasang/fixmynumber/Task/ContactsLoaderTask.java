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

/**
 * Created by Raenar on 28.10.2015.
 */
public class ContactsLoaderTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    ArrayList<ContactItem> contactItems;
    AlertDialog dialog;

    public ContactsLoaderTask(Activity activity, ArrayList<ContactItem> contactItems) {
        this.activity = activity;
        this.contactItems = contactItems;
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
                contactItems.add(contactItem);
            }
        }
        numbersCursor.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((FragmentActivity) activity).updateUI();
    }
}
