package com.khasang.fixmynumber.Service;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Raenar on 01.12.2015.
 */
public interface ServiceCallbackListener {
    void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle data);
}
