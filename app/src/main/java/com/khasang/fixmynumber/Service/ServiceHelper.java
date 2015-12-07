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

    private Intent createIntent(final Context context, String actionLogin, final int requestId) {
        Intent i = new Intent(context,TestService.class);
        i.setAction(actionLogin);

        i.putExtra(TestService.EXTRA_STATUS_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Intent originalIntent = pendingActivities.get(requestId);
                if (isPending(requestId)) {
                    if (resultCode != TestIntentHandler.PROGRESS_CODE) {
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

    public int doLoadAction() {
        final int requestId = createId();
        Intent i = createIntent(application, TestIntentHandler.ACTION_LOAD, requestId);
        return runRequest(requestId, i);
    }

    public int doSaveAction(ArrayList<ContactItem> contacts) {
        final int requestId = createId();
        Intent i = createIntent(application, TestIntentHandler.ACTION_SAVE, requestId);
        i.putParcelableArrayListExtra(TestIntentHandler.SAVED_LIST_KEY, contacts);
        return runRequest(requestId, i);
    }
}
