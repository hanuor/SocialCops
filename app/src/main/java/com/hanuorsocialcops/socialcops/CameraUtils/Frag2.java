package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.hanuorsocialcops.socialcops.MainActivity;
import com.hanuorsocialcops.socialcops.R;
import com.hanuorsocialcops.socialcops.Utils.InformationHandler;
import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Shantanu Johri on 9/20/2016.
 */

public class Frag2 extends AppCompatActivity {
    File [] mediaFiles;
    File imageDir;
    static GridView gridView;
    ImageAdapter adapter;
    TextView toolbarT;
    FloatingActionButton uploadButton;
    ArrayList<String> fileP = new ArrayList<String>();
    ArrayList<String> selectedS = new ArrayList<String>();
    private InformationHandler stickyEvent;
    ArrayList<Integer> countC;
    private String username = null;
    Toolbar tb;
    boolean checker;

    private boolean checkWriteExternalPermission()
    { boolean counter = true;
        String permission1 = "android.permission.WRITE_EXTERNAL_STORAGE";
        String permission2 = "android.permission.CAMERA";

        String permission3 = "android.permission.READ_EXTERNAL_STORAGE";

        String permission5 = "android.permission.RECORD_AUDIO";
        int res = this.checkCallingOrSelfPermission(permission1);

        int res2 = this.checkCallingOrSelfPermission(permission2);
        int res3 = this.checkCallingOrSelfPermission(permission3);

        int res5 = this.checkCallingOrSelfPermission(permission5);
        if(res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED &&
                res3 == PackageManager.PERMISSION_GRANTED
                && res5 == PackageManager.PERMISSION_GRANTED){

        }else{
            counter = false;
        }
        return counter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag2_fragmet);
        setTheme(R.style.MainActivityTheme);
        countC = new ArrayList<Integer>();
        checker = false;
        if (checkWriteExternalPermission()){

        }else{
            Toast.makeText(this, "Please grant the necessary permissions", Toast.LENGTH_SHORT).show();
        }
        stickyEvent = EventBus.getDefault().getStickyEvent(InformationHandler.class);
        if(stickyEvent != null) {
            username = stickyEvent.getKEYID();
        }
        tb = (Toolbar) findViewById(R.id.tooolbar);
        toolbarT = (TextView) findViewById(R.id.toolbarText);
        toolbarT.setTextSize(22);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        uploadButton = (FloatingActionButton) findViewById(R.id.uploadButton);
        //Typeface myTypeface = Typeface.createFromAsset(Frag2.this.getAssets(), "fonts/dejavu.ttf");
        //toolbarT.setTypeface(myTypeface);
        imageDir = new File(Environment.getExternalStorageDirectory().toString()+
                "/socialCopsDemo/");
        mediaFiles = imageDir.listFiles();
        for (int i = 0; i < mediaFiles.length; i++)
        {

            fileP.add(mediaFiles[i].getAbsolutePath());

        }
        if((imageDir.exists()))
        {

            adapter = new ImageAdapter(Frag2.this, fileP);
            gridView = (GridView) findViewById(R.id.gridview);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    // Toast.makeText(Frag2.this, ""+position, Toast.LENGTH_SHORT).show();

                    int selectedIndex = adapter.selectedPositions.indexOf(position);
                        int length = countC.size();
                        for(int k = 0; k< length; k++){
                            if(countC.get(k) == position){
                                checker = true;
                            }
                        }
                        if(!checker) {
                            countC.add(position);
                            uploadButton.setBackgroundDrawable(Frag2.this.getResources().getDrawable(R.drawable.cloudstorage));
                            uploadButton.setVisibility(View.VISIBLE);
                            adapter.selectedPositions.add(selectedIndex);
                            ((CustomView) arg1).display(true);
                            selectedS.add(fileP.get(position));

                    }
                }
            });
        }//if
        else
        {
            //   setContentView(R.layout.no_media);
        }
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Frag2.this, "Selected item/s are being uploaded...", Toast.LENGTH_SHORT).show();

                Intent inn = new Intent(Frag2.this, UploadFrag.class);
                inn.putStringArrayListExtra("SelectedS", selectedS);
                Frag2.this.startActivity(inn);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent mok = new Intent(Frag2.this, MainActivity.class);
            startActivity(mok);
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
