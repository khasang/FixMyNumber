package com.khasang.fixmynumber.Robospice;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * Created by Raenar on 26.11.2015.
 */
public class TestSpiceRequest extends SpiceRequest<String> {

    public TestSpiceRequest() {
        super(String.class);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        for (int i = 0; i < 5; i++) {
            Thread.sleep(100);
            publishProgress(i);
        }
        return "ok";
    }
}
