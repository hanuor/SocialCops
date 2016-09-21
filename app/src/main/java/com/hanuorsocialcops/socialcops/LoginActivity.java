package com.hanuorsocialcops.socialcops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

/**
 * Created by Shantanu Johri on 9/21/2016.
 */

public class LoginActivity extends AppCompatActivity {
    Client mKinveyClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , LoginActivity.this).build();
        setContentView(R.layout.loginscreen);
        mKinveyClient.user().retrieve(new KinveyUserCallback() {
            @Override
            public void onFailure(Throwable e) {
                Log.d("han","ss1");
                mKinveyClient.user().create("hanuor", "han11", new KinveyUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("han","ss2");
                        Toast.makeText(MainActivity.this, "YEs", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("han","ss3");
                    }
                });

            }
            @Override
            public void onSuccess(User user) {
                mKinveyClient.user().login(new KinveyUserCallback() {
                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("han","ss5");
                        CharSequence text = "Login error.";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(User u) {
                        Log.d("han","ss6");
                        CharSequence text = "Welcome back!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    

    }
}
