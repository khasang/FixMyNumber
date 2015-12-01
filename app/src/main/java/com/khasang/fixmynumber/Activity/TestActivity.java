package com.khasang.fixmynumber.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.khasang.fixmynumber.R;

public class TestActivity extends BaseServiceActivity {
    Button testButton;
    TextView testProgress;
    TextView testResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testButton = (Button) findViewById(R.id.testButton);
        testProgress = (TextView) findViewById(R.id.testProgress);
        testResult = (TextView) findViewById(R.id.testResult);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        
    }
}
