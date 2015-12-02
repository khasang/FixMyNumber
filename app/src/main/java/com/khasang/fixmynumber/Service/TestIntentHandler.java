package com.khasang.fixmynumber.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by Raenar on 01.12.2015.
 */
public class TestIntentHandler extends BaseIntentHandler {
    public static final String ACTION_TEST = "action_1";
    public static final String TEST_ID = "id";
//    public static final int PROGRESS_CODE = 1;
    public static final String PROGRESS_KEY = "progress";


    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        for (int i = 0; i < 5; i++) {
            Log.d("testService", "i+1 = "+ (i+1));
            Bundle bundle = new Bundle();
            bundle.putInt(PROGRESS_KEY, (i + 1));
            callback.send((i+2),bundle);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
