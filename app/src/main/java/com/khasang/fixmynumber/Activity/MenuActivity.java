package com.khasang.fixmynumber.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.khasang.fixmynumber.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonContacts = (Button) findViewById(R.id.buttonStartChange);
        buttonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
                startActivity(intent);
            }
        });
        Button buttonRestore = (Button) findViewById(R.id.buttonStartRestore);
        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RestoreContactsActivity.class);
                startActivity(intent);
            }
        });
    }
}
