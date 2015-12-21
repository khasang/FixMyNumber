package com.khasang.fixmynumber.Helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.khasang.fixmynumber.Activity.FragmentActivity;
import com.khasang.fixmynumber.R;

/**
 * Created by Raenar on 28.10.2015.
 */
public class DialogCreator {


    public static final int LOADING_DIALOG = 0;
    public static final int BACKUP_DIALOG = 1;
    public static final int SAVING_DIALOG = 2;
    public static final int INFO_DIALOG = 3;

    public DialogCreator() {
    }

    public static AlertDialog createDialog(Activity activity, int type) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity, android.R.style.Theme_Holo);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        LayoutInflater inflater = activity.getLayoutInflater();
        switch (type) {
            case LOADING_DIALOG:
                builder.setView(inflater.inflate(R.layout.loading_dialog, null));
                break;
            case BACKUP_DIALOG:
                builder.setView(inflater.inflate(R.layout.backup_dialog, null));
                break;
            case SAVING_DIALOG:
                builder.setView(inflater.inflate(R.layout.saving_dialog, null));
                break;
            case INFO_DIALOG:
                final SharedPreferences sharedPreferences = activity.getSharedPreferences(FragmentActivity.FILE_NAME, FragmentActivity.MODE_PRIVATE);
                View view = View.inflate(activity, R.layout.info_dialog_view, null);
                final CheckBox infoDialogCheckBox = (CheckBox) view.findViewById(R.id.infoDialogCheckBox);
                builder
                        .setMessage(R.string.info_dialog_message)
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (infoDialogCheckBox.isChecked()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(FragmentActivity.DATA_MAP_KEY, true);
                                    editor.apply();
                                }
                                dialog.dismiss();
                            }
                        });
                break;
        }
        return builder.create();
    }
}
