package com.khasang.fixmynumber.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * Created by Raenar on 01.12.2015.
 */
public abstract class BaseIntentHandler {
    public static final int SUCCESS_RESPONSE = 0;

    public static final int FAILURE_RESPONSE = 1;

    public final void execute(Intent intent, Context context, ResultReceiver callback) {
        this.callback = callback;
        doExecute(intent, context, callback);
    }

    public abstract void doExecute(Intent intent, Context context, ResultReceiver callback);

    private ResultReceiver callback;

    private int result;

    public int getResult() {
        return result;
    }

    protected void sendUpdate(int resultCode, Bundle data) {
        result = resultCode;
        if (callback != null) {
            callback.send(resultCode, data);
        }
    }

}
