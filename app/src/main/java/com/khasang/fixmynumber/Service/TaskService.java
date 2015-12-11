package com.khasang.fixmynumber.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Raenar on 01.12.2015.
 */
public class TaskService extends IntentService {

    public static final String EXTRA_STATUS_RECEIVER = "receiver";

    public TaskService() {
        super("TaskService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
//            if (IntentHandler.ACTION_LOAD.equals(action)) {
//                new IntentHandler().execute(intent, getApplicationContext(), receiver);
//            }
            new IntentHandler().execute(intent, getApplicationContext(), receiver);
        }
    }
}
