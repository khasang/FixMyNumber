package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.khasang.fixmynumber.Model.ContactItem;

import java.util.ArrayList;

/**
 * Created by Raenar on 28.10.2015.
 */
public class ContactsSaverTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    ArrayList<ContactItem> contactItems;

    public ContactsSaverTask(Activity activity, ArrayList<ContactItem> contactItems) {
        this.activity = activity;
        this.contactItems = contactItems;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < contactItems.size(); i++) {
            if (contactItems.get(i).getNumberNew() != null) {
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                                new String[]{contactItems.get(i).getNumberOriginal()})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactItems.get(i).getNumberNew())
                        .build());
                try {
                    activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
                    Log.d("ContactsSaverTask", "changed " + contactItems.get(i).getName()
                            + " " + contactItems.get(i).getNumberOriginal()
                            + " => to " + contactItems.get(i).getNumberNew());
                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            } else {
                Log.d("ContactsSaverTask", "Unchanged: " + contactItems.get(i).getName());
            }
        }
        return null;
    }
}
