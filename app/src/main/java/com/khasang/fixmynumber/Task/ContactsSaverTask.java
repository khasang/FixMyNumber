package com.khasang.fixmynumber.Task;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.net.Uri;
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
    ArrayList<ContactItem> contactList;

    public ContactsSaverTask(Activity activity, ArrayList<ContactItem> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getNumberNew() != null) {
                if (contactList.get(i).getAccountType().equals("sim")) {
                    Uri uri = Uri.parse("content://icc/adn");
                    ContentValues cv = new ContentValues();
                    cv.put("tag", contactList.get(i).getName());
                    cv.put("number", contactList.get(i).getNumberOriginal());
                    cv.put("newTag", contactList.get(i).getName());
                    cv.put("newNumber", contactList.get(i).getNumberNew());
                    activity.getContentResolver().update(uri, cv, null, null);
                }
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                                new String[]{contactList.get(i).getNumberOriginal()})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactList.get(i).getNumberNew())
                        .build());
                try {
                    activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
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
        return null;
    }
}
