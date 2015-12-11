package com.khasang.fixmynumber.Activity;

import android.app.Application;

import com.khasang.fixmynumber.Service.ServiceHelper;

/**
 * Created by Raenar on 03.12.2015.
 */
public class ServiceApplication extends Application {
    private ServiceHelper serviceHelper ;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceHelper = new ServiceHelper(this);
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }
}
