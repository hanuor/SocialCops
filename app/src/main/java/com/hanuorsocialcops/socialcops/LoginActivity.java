package com.hanuorsocialcops.socialcops;

import android.app.ProgressDialog;
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
import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.hanuorsocialcops.socialcops.Utils.InformationHandler;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

import de.greenrobot.event.EventBus;

/**
 * Created by Shantanu Johri on 9/21/2016.
 */

public class LoginActivity extends AppCompatActivity {
    Client mKinveyClient;
    RelativeLayout accounnt_buttons;
    TextInputLayout til, tpl;
    Button login, signUp;
    ProgressDialog pd;
    private static EventBus bus = EventBus.getDefault();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , LoginActivity.this).build();
        setContentView(R.layout.loginscreen);
        setTheme(R.style.MainActivityTheme);
        pd = new ProgressDialog(LoginActivity.this);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/dejavu.ttf");
        final TextView myTextView = (TextView)findViewById(R.id.tv);
        til = (TextInputLayout) findViewById(R.id.input_layout_username);
        tpl = (TextInputLayout) findViewById(R.id.input_layout_password);
        accounnt_buttons = (RelativeLayout) findViewById(R.id.account_buttons);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);
        pd.setMessage("Hold up a moment please ...");

        myTextView.setTextSize(80);
        myTextView.setText("SocialCops");
        myTextView.setTextColor(Color.parseColor("#ffffff"));
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
                bus.postSticky(new InformationHandler(user.getUsername()));
                Intent newIn = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(newIn);
                finish();

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                til.setVisibility(View.VISIBLE);
                til.setAnimation(fadeIn);

                tpl.setVisibility(View.VISIBLE);
                tpl.setAnimation(fadeIn);

                accounnt_buttons.setVisibility(View.VISIBLE);
                accounnt_buttons.setAnimation(fadeIn);

            }
        }, 2000);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if(tpl.getEditText().getText().length()!=0 && til.getEditText().getText().length()!=0){
                    mKinveyClient.user().login(til.getEditText().getText().toString(), tpl.getEditText().getText().toString(), new KinveyUserCallback() {
                        @Override
                        public void onFailure(Throwable t) {
                            pd.dismiss();
                            CharSequence text = "Wrong username or password.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onSuccess(User u) {
                            pd.dismiss();
                            CharSequence text = "Welcome back " + u.getUsername();
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            bus.postSticky(new InformationHandler(u.getUsername()));
                            Intent newIn = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(newIn);
                            finish();
                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Please fill up the blanks above", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if(tpl.getEditText().getText().length()!=0 && til.getEditText().getText().length()!=0){
                    mKinveyClient.user().create(til.getEditText().getText().toString(), tpl.getEditText().getText().toString(), new KinveyUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                          pd.dismiss();
                            bus.postSticky(new InformationHandler(user.getUsername()));
                            Intent newIn = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(newIn);
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            pd.dismiss();
                            CharSequence text = "Could not sign up.";
                            Toast.makeText(getApplicationContext(), text , Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Please fill up the blanks above", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
