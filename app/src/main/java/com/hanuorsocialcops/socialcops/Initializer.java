package com.hanuorsocialcops.socialcops;

import android.app.Application;

import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;

/**
 * Created by Shantanu Johri on 9/21/2016.
 */

public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final Client mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , this.getApplicationContext()).build();
    }
}
