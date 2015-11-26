package com.khasang.fixmynumber.Robospice;

import android.app.Application;
import android.content.Context;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;

/**
 * A simple service that allows offline requests to be executed.
 *
 * @author sni
 *
 */
public class TestOfflineSpiceService extends SpiceService {

    @Override
    public CacheManager createCacheManager( Application application ) {
        return new CacheManager();
    }

    @Override
    protected NetworkStateChecker getNetworkStateChecker() {
        return new NetworkStateChecker() {

            @Override
            public boolean isNetworkAvailable( Context context ) {
                return true;
            }

            @Override
            public void checkPermissions( Context context ) {
                // do nothing
            }
        };
    }

}
