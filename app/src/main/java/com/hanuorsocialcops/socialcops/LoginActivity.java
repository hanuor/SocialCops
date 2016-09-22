package com.hanuorsocialcops.socialcops;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    RelativeLayout accounnt_buttons;
    TextInputLayout til, tpl;
    Button login, signUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , LoginActivity.this).build();
        setContentView(R.layout.loginscreen);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/dejavu.ttf");
        final TextView myTextView = (TextView)findViewById(R.id.tv);
        til = (TextInputLayout) findViewById(R.id.input_layout_username);
        tpl = (TextInputLayout) findViewById(R.id.input_layout_password);
        accounnt_buttons = (RelativeLayout) findViewById(R.id.account_buttons);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);

        myTextView.setTextSize(80);
        myTextView.setText("SocialCops");
        myTextView.setTypeface(myTypeface);
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        myTextView.setAnimation(fadeIn);
        mKinveyClient.user().retrieve(new KinveyUserCallback() {
            @Override
            public void onFailure(Throwable e) {
                Log.d("ham", "" + e.getMessage());
            }
            @Override
            public void onSuccess(User user) {
                
                //get active uesr id and put it in the event bus!!!
                Intent newIn = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(newIn);

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(1000);
                fadeOut.setDuration(1000);
               */
                //myTextView.setAnimation(fadeOut);
                //myTextView.setVisibility(View.GONE);
                til.setVisibility(View.VISIBLE);
                til.setAnimation(fadeIn);

                tpl.setVisibility(View.VISIBLE);
                tpl.setAnimation(fadeIn);

                accounnt_buttons.setVisibility(View.VISIBLE);
                accounnt_buttons.setAnimation(fadeIn);

               // signUp.setVisibility(View.VISIBLE);
                //signUp.setAnimation(fadeIn);
                //signUp.setTextSize(30);

            }
        }, 2000);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpl.getEditText().getText()!=null && til.getEditText().getText()!=null){
                    mKinveyClient.user().login(til.getEditText().getText().toString(), tpl.getEditText().getText().toString(), new KinveyUserCallback() {
                        @Override
                        public void onFailure(Throwable t) {
                            CharSequence text = "Wrong username or password.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onSuccess(User u) {
                            CharSequence text = "Welcome back," + u.getUsername() + ".";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpl.getEditText().getText()!=null && til.getEditText().getText()!=null){
                    mKinveyClient.user().create(til.getEditText().getText().toString(), tpl.getEditText().getText().toString(), new KinveyUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            CharSequence text = user.getUsername() + ", your account has been created.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            CharSequence text = "Could not sign up.";
                            Log.d("ham",""+throwable.getMessage());
                            Toast.makeText(getApplicationContext(), text + " " + throwable.getMessage() , Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });

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
