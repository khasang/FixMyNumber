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
public class TestService extends IntentService {

    public static final String EXTRA_STATUS_RECEIVER = "receiver";

    public TestService() {
        super("TestService");
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
//            if (TestIntentHandler.ACTION_LOAD.equals(action)) {
//                new TestIntentHandler().execute(intent, getApplicationContext(), receiver);
//            }
//            // .....
            new TestIntentHandler().execute(intent, getApplicationContext(), receiver);
        }
    }
}
