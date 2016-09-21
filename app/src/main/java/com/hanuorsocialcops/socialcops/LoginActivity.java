package com.hanuorsocialcops.socialcops;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

/**
 * Created by Shantanu Johri on 9/21/2016.
 */

public class LoginActivity extends AppCompatActivity {
    Client mKinveyClient;
    KenBurnsView kbv;
    TextView signUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , LoginActivity.this).build();
        setContentView(R.layout.loginscreen);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/dejavu.ttf");
        final TextView myTextView = (TextView)findViewById(R.id.tv);
        signUp = (TextView) findViewById(R.id.signup);

        myTextView.setTextSize(80);
        myTextView.setText("SocialCops");
        myTextView.setTypeface(myTypeface);
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        myTextView.setAnimation(fadeIn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(1000);
                fadeOut.setDuration(1000);
                myTextView.setAnimation(fadeOut);
                myTextView.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);
                signUp.setAnimation(fadeIn);

            }
        }, 2000);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

       /* mKinveyClient.user().retrieve(new KinveyUserCallback() {
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

*/
    }
}
