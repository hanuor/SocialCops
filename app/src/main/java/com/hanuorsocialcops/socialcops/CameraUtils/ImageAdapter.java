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

import static android.R.attr.path;

/**
 * Created by Shantanu Johri on 9/23/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<String> f = new ArrayList<String>();
    private Context ctx;

    public ImageAdapter(Context ctx, ArrayList<String> f) {
        this.ctx = ctx;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.galleryitem, null);
            holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


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
          holder.imageview.setImageBitmap(myBitmap);
        //holder.imageview.setImageBitmap(myBitmap);
        return convertView;
    }
}
class ViewHolder {
    ImageView imageview;


}
