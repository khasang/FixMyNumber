package com.khasang.fixmynumber.Helper;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.khasang.fixmynumber.R;

/**
 * Created by Raenar on 28.10.2015.
 */
public class LoadingDialogCreator {
    Activity activity;

    public LoadingDialogCreator(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog createLoadingDialog(){
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity, android.R.style.Theme_Holo);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        return builder.create();
    }
}
