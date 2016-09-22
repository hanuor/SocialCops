package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hanuorsocialcops.socialcops.R;

import java.util.ArrayList;

/**
 * Created by Shantanu Johri on 9/23/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<String> f = new ArrayList<String>();

    public ImageAdapter(Context ctx, ArrayList<String> f) {
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


        Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
        holder.imageview.setImageBitmap(myBitmap);
        return convertView;
    }
}
class ViewHolder {
    ImageView imageview;


}
