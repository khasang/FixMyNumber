package com.khasang.fixmynumber.Activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khasang.fixmynumber.Service.ServiceCallbackListener;
import com.khasang.fixmynumber.Service.ServiceHelper;

/**
 * Created by Raenar on 01.12.2015.
 */
public abstract class BaseServiceActivity extends AppCompatActivity implements ServiceCallbackListener {
    private ServiceHelper serviceHelper ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceHelper = new ServiceHelper(getApplication());
        //todo make custom app class to store ServiceHelper
    }

    protected void onResume() {
        super.onResume();
        serviceHelper.addListener(this);
    }

    protected void onPause() {
        super.onPause();
        serviceHelper.removeListener(this);
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    public abstract void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData);
}

