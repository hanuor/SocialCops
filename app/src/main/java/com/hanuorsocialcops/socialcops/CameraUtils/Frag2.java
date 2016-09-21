package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.hanuorsocialcops.socialcops.Credentials.CredentialManager;
import com.hanuorsocialcops.socialcops.R;
import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shantanu Johri on 9/20/2016.
 */

//for videos
    //Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//use hashmap
public class Frag2 extends Fragment {
    String SCAN_PATH;
    File[] allFiles ;
    File [] mediaFiles;
    File imageDir;
    static GridView gridView;
    ImageAdapter adapter;
    Intent in;
    ImageButton btncam;
    String name = null;
    ArrayList<Bitmap> bmpArray = new ArrayList<Bitmap>();
    ArrayList<String> fileP = new ArrayList<String>();
    public static final String TAG = "Album3Activity";
    Client mKinveyClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mKinveyClient = new Client.Builder(CredentialManager.appID(), CredentialManager.appSecret()
                , getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag2_fragmet,container,false);
        CheckableLayout l;

        imageDir = new File(Environment.getExternalStorageDirectory().toString()+
                "/socialCopsDemo");
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
            gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
            gridView.setMultiChoiceModeListener(new MultimodeChoiceListener());
           /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {


                   File upFile = new File(fileP.get(position));
                    mKinveyClient.file().upload(upFile, new UploaderProgressListener() {
                        @Override
                        public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {

                        }

                        @Override
                        public void onSuccess(FileMetaData fileMetaData) {
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(getActivity(), ""+throwable, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                }
            });*/
        }//if
        else
        {
         //   setContentView(R.layout.no_media);
        }
        return v;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public class MultimodeChoiceListener implements
            GridView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            int selectCount = gridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }
        }

    }
    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        @SuppressWarnings("deprecation")
        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackgroundDrawable(checked ? getResources().getDrawable(
                    R.drawable.backblue) : null);
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }
    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ArrayList<String> f = new ArrayList<String>();

        public ImageAdapter(Context ctx,ArrayList<String> f) {
            this.f = f;
            mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            CheckableLayout l;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.imageview.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
                l = new CheckableLayout(getActivity());
                l.setLayoutParams(new GridView.LayoutParams(
                        GridView.LayoutParams.WRAP_CONTENT,
                        GridView.LayoutParams.WRAP_CONTENT));
               // if(l.getParent()!=null)
                 //   ((ViewGroup)l.getParent()).removeView(l); // <- fix
              //  l.addView(holder.imageview);
               // l.addView(holder.imageview);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
                l = (CheckableLayout) convertView;
                holder.imageview = (ImageView) l.getChildAt(0);
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;


    }

}
