package com.hanuorsocialcops.socialcops;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.hanuorsocialcops.socialcops.CameraUtils.CameraDetails;
import com.hanuorsocialcops.socialcops.CameraUtils.CameraFragment;
import com.hanuorsocialcops.socialcops.CameraUtils.Frag2;
import com.hanuorsocialcops.socialcops.CameraUtils.UploadFrag;
import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    BottomBar bottomBar;
    Button but;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.RECORD_AUDIO};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        final Client mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , this.getApplicationContext()).build();
        Log.d("han","ss");

        setContentView(R.layout.activity_main);
        setTheme(R.style.MainActivityTheme);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
                    case R.id.tab_favorites:
                        CameraFragment frag1=new CameraFragment();
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentContainer,frag1,"p");
                        fragmentTransaction.commit();
                        break;
                    case R.id.tab_nearby:
                        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.RECORD_AUDIO};

                        if(hasPermissions(MainActivity.this, PERMISSIONS)){
                            Frag2 frag2 = new Frag2();
                            FragmentManager fragmentManager1 = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                            fragmentTransaction1.replace(R.id.contentContainer, frag2, "nearby");
                            fragmentTransaction1.commit();
                          }else {
                            Toast.makeText(MainActivity.this, "Can't access. Please grant the necessary permissions", Toast.LENGTH_SHORT).show();

                        }
                        break;


                    default:
                        CameraFragment frag3=new CameraFragment();
                        FragmentManager fragmentManager3=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2=fragmentManager3.beginTransaction();
                        fragmentTransaction2.replace(R.id.contentContainer,frag3);
                        fragmentTransaction2.commit();
                        break;

                }
            }
        });
    }
}
