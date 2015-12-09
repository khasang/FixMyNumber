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

    public LoadingDialogCreator() {
    }

    public static AlertDialog createLoadingDialog(Activity activity, int version){
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity, android.R.style.Theme_Holo);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        LayoutInflater inflater = activity.getLayoutInflater();
        switch (version){
            case 0:
                builder.setView(inflater.inflate(R.layout.loading_dialog, null));
                break;
            case 1:
                builder.setView(inflater.inflate(R.layout.loading_dialog2, null));
                break;
        }
        return builder.create();
    }
}
