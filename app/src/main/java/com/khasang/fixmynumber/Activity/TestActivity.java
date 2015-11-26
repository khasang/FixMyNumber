package com.khasang.fixmynumber.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khasang.fixmynumber.R;
import com.khasang.fixmynumber.Robospice.TestOfflineSpiceRequest;
import com.khasang.fixmynumber.Robospice.TestSpiceRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;

import roboguice.util.temp.Ln;

public class TestActivity extends AppCompatActivity {
    Button testButton;
    TextView progressText;
    TextView resultText;
    private SpiceManager spiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testButton = (Button) findViewById(R.id.buttonTest);
        progressText = (TextView) findViewById(R.id.textViewProgress);
        resultText = (TextView) findViewById(R.id.textViewResult);
        spiceManager = new SpiceManager(TestOfflineSpiceRequest.class);
    }

    public void testClick(View view) {
        resultText.setText("");
        spiceManager.execute(new TestSpiceRequest(), new TestRequestListener());
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }


    public final class TestRequestListener implements RequestListener<String>, RequestProgressListener {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(TestActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(String s) {
            resultText.setText("Success");
            if (s.equals("ok")) {
                resultText.setText("Success + OK");
            }
        }


        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
//            Ln.d("Binary progress : %s = %d", progress.getStatus(), Math.round(100 * progress.getProgress()));
            progressText.setText(" " + progress.getProgress());
        }
    }
}
