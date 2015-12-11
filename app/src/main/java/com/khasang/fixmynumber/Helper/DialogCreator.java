package com.khasang.fixmynumber.Helper;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.khasang.fixmynumber.R;

/**
 * Created by Raenar on 28.10.2015.
 */
public class DialogCreator {


    public static final int LOADING_DIALOG = 0;
    public static final int BACKUP_DIALOG = 1;
    public static final int SAVING_DIALOG = 2;

    public DialogCreator() {
    }

    public static AlertDialog createDialog(Activity activity, int type){
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity, android.R.style.Theme_Holo);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        LayoutInflater inflater = activity.getLayoutInflater();
        switch (type){
            case LOADING_DIALOG:
                builder.setView(inflater.inflate(R.layout.loading_dialog, null));
                break;
            case BACKUP_DIALOG:
                builder.setView(inflater.inflate(R.layout.backup_dialog, null));
                break;
            case SAVING_DIALOG:
                builder.setView(inflater.inflate(R.layout.saving_dialog, null));
                break;
        }
        return builder.create();
    }
}
