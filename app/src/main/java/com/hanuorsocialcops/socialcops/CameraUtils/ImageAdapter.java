package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hanuorsocialcops.socialcops.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.path;

/**
 * Created by Shantanu Johri on 9/23/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<String> f = new ArrayList<String>();
    List<Integer> selectedPositions = new ArrayList<>();

    private Context ctx;

    public ImageAdapter(Context ctx, ArrayList<String> f) {
        this.ctx = ctx;
        this.f = f;
        //mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        CustomView customView = (convertView == null) ?
                new CustomView(ctx) : (CustomView) convertView;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 3;
        options.inPurgeable = true;
        Bitmap myBitmap = null;
        if(f.get(position).contains(".mp4"))
        {
            myBitmap = ThumbnailUtils.createVideoThumbnail(f.get(position), 0); //Creation of Thumbnail of video
        }else{
            myBitmap = BitmapFactory.decodeFile(f.get(position), options);
        }
        customView.display(myBitmap, selectedPositions.contains(position));
        return customView;
    }
}
