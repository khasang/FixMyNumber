package com.khasang.fixmynumber.Service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.SparseArray;

import com.khasang.fixmynumber.Model.ContactItem;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Raenar on 01.12.2015.
 */
public class ServiceHelper {
    private ArrayList<ServiceCallbackListener> currentListeners = new ArrayList<ServiceCallbackListener>();

    private AtomicInteger idCounter = new AtomicInteger();

    private SparseArray<Intent> pendingActivities = new SparseArray<Intent>();

    private Application application;

    public ServiceHelper(Application app) {
        this.application = app;
    }

    public void addListener(ServiceCallbackListener currentListener) {
        currentListeners.add(currentListener);
    }

    public void removeListener(ServiceCallbackListener currentListener) {
        currentListeners.remove(currentListener);
    }

    public boolean isPending(int requestId) {
        return pendingActivities.get(requestId) != null;
    }

    private int createId() {
        return idCounter.getAndIncrement();
    }

    private int runRequest(final int requestId, Intent i) {
        pendingActivities.append(requestId, i);
        application.startService(i);
        return requestId;
    }

    private Intent createIntent(final Context context, String action, final int requestId) {
        Intent i = new Intent(context, TaskService.class);
        i.setAction(action);

        i.putExtra(TaskService.EXTRA_STATUS_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Intent originalIntent = pendingActivities.get(requestId);
                if (isPending(requestId)) {
                    if (resultCode != IntentHandler.PROGRESS_CODE) {
                        pendingActivities.remove(requestId);
                    }
                    for (ServiceCallbackListener currentListener : currentListeners) {
                        if (currentListener != null) {
                            currentListener.onServiceCallback(requestId, originalIntent, resultCode, resultData);
                        }
                    }
                }
            }
        });
        return i;
    }

    public int loadContacts() {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_LOAD, requestId);
        return runRequest(requestId, i);
    }

    public int saveContacts(ArrayList<ContactItem> contacts) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_SAVE, requestId);
        i.putParcelableArrayListExtra(IntentHandler.SAVED_LIST_KEY, contacts);
        return runRequest(requestId, i);
    }

    public int backupContacts(ArrayList<ContactItem> contacts) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_BACKUP, requestId);
        i.putParcelableArrayListExtra(IntentHandler.BACKUP_LIST_KEY, contacts);
        return runRequest(requestId, i);
    }

    public int getBackupList() {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_GET_BACKUP, requestId);
        return runRequest(requestId, i);
    }

    public int loadBackup(String tableName) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_LOAD_BACKUP, requestId);
        i.putExtra(IntentHandler.TABLE_NAME_KEY, tableName);
        return runRequest(requestId, i);
    }

    public int deleteBackup(String tableName) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_DELETE_BACKUP, requestId);
        i.putExtra(IntentHandler.TABLE_NAME_KEY, tableName);
        return runRequest(requestId, i);
    }

    public int fillContactsFromBackup(ArrayList<ContactItem> contacts, String tableName) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_GET_CONTACTS_BACKUP, requestId);
        i.putParcelableArrayListExtra(IntentHandler.LIST_TO_SHOW_KEY, contacts);
        i.putExtra(IntentHandler.TABLE_NAME_KEY, tableName);
        return runRequest(requestId, i);
    }

    public int findDuplicates(ArrayList<ContactItem> contacts) {
        final int requestId = createId();
        Intent i = createIntent(application, IntentHandler.ACTION_FIND_DUPLICATES, requestId);
        i.putParcelableArrayListExtra(IntentHandler.LIST_TO_SHOW_KEY, contacts);
        return runRequest(requestId, i);
    }
}
