package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.content.Intent;
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

//for videos
    //Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//use hashmap
public class Frag2 extends Fragment {
    File [] mediaFiles;
    File imageDir;
    static GridView gridView;
    ImageAdapter adapter;
    TextView toolbarT;
    FloatingActionButton uploadButton;
    ArrayList<String> fileP = new ArrayList<String>();
    ArrayList<String> selectedS = new ArrayList<String>();
    private InformationHandler stickyEvent;
    private String username = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.frag2_fragmet,container,false);
        stickyEvent = EventBus.getDefault().getStickyEvent(InformationHandler.class);
        if(stickyEvent != null) {
            username = stickyEvent.getKEYID();
            Toast.makeText(getActivity(), ""+username, Toast.LENGTH_SHORT).show();
            }
                  toolbarT = (TextView) v.findViewById(R.id.toolbarText);
        toolbarT.setTextSize(25);
        uploadButton = (FloatingActionButton) v.findViewById(R.id.uploadButton);
        //Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/dejavu.ttf");
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

            adapter = new ImageAdapter(getActivity(), fileP);
            gridView = (GridView)v.findViewById(R.id.gridview);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                   // Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();

                    int selectedIndex = adapter.selectedPositions.indexOf(position);
                    if (selectedIndex > -1) {
                        adapter.selectedPositions.remove(selectedIndex);
                        ((CustomView)arg1).display(false);
                       selectedS.remove(fileP.get(position));
                    } else {
                        uploadButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.cloudstorage));
                        uploadButton.setVisibility(View.VISIBLE);
                        adapter.selectedPositions.add(selectedIndex);
                        ((CustomView)arg1).display(true);
                        selectedS.add(fileP.get(position));
                        //selectedS.add((String) arg0.getItemAtPosition(position));
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
                Toast.makeText(getActivity(), "Selected item/s are being uploaded...", Toast.LENGTH_SHORT).show();

               Intent inn = new Intent(getActivity(), UploadFrag.class);
                inn.putStringArrayListExtra("SelectedS", selectedS);
                getActivity().startActivity(inn);
                 }
        });
        return v;
    }
}
