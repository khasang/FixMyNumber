package com.khasang.fixmynumber.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khasang.fixmynumber.R;

public class MenuActivity extends AppCompatActivity {

    private static final int PERMISSIONS_CONTACTS_CHANGE_CODE = 0;
    private static final int PERMISSIONS_CONTACTS_RESTORE_CODE = 1;
    public static final int PERMISSIONS_CONTACTS_DUPLICATES_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonContacts = (Button) findViewById(R.id.buttonStartChange);
        buttonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_CONTACTS_CHANGE_CODE);

                } else {
                    Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                    startActivity(intent);
                }
            }
        });
        Button buttonRestore = (Button) findViewById(R.id.buttonStartRestore);
        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_CONTACTS_RESTORE_CODE);

                } else {
                    Intent intent = new Intent(getApplicationContext(), RestoreContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
        Button buttonDuplicates = (Button) findViewById(R.id.buttonStartDuplicates);
        buttonDuplicates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_CONTACTS_DUPLICATES_CODE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), DuplicatesActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_CONTACTS_CHANGE_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
            startActivity(intent);
        }
        if (requestCode == PERMISSIONS_CONTACTS_RESTORE_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getApplicationContext(), RestoreContactsActivity.class);
            startActivity(intent);
        }
        if (requestCode == PERMISSIONS_CONTACTS_DUPLICATES_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getApplicationContext(), DuplicatesActivity.class);
            startActivity(intent);
        }
    }
}
