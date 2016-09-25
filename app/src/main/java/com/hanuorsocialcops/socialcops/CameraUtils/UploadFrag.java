package com.hanuorsocialcops.socialcops.CameraUtils;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.hanuorsocialcops.socialcops.R;
import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Shantanu Johri on 9/25/2016.
 */

public class UploadFrag extends AppCompatActivity {
    ArrayList<String> receivedStr;
    ArrayList<String> copyAdap;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadfrag);
       toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Client mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , UploadFrag.this).build();

        final GridView up = (GridView) findViewById(R.id.uploaded);
        final GridView rem = (GridView) findViewById(R.id.remaining);
        receivedStr = new ArrayList<String>();
        copyAdap = new ArrayList<String>();
        String root = Environment.getExternalStorageDirectory().toString()+"/socialCopsDemo/";
        Log.d("frooo",""+root);
        receivedStr = getIntent().getStringArrayListExtra("SelectedS");
        final GlobalAdapter globalAdapter = new GlobalAdapter(UploadFrag.this, receivedStr);
        rem.setAdapter(globalAdapter);

        final GlobalAdapter globalAdapter2 = new GlobalAdapter(UploadFrag.this, copyAdap);
        up.setAdapter(globalAdapter2);
        for(int i=receivedStr.size()-1; i>=0;i--){
            Log.d("frooo1",""+receivedStr.get(i));

            File upFile = new File(receivedStr.get(i));
            // file.createNewFile();
            final int finalI = i;
            mKinveyClient.file().upload(upFile, new UploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
                   }


                @Override
                public void onSuccess(FileMetaData fileMetaData) {
                    try {
                        copyAdap.add(receivedStr.get(finalI));
                        receivedStr.remove(finalI);
                        if(receivedStr.size() == 0){
                            Toast.makeText(UploadFrag.this, "All item/s uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                        globalAdapter.notifyDataSetChanged();
                        globalAdapter2.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                 }


            });

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
