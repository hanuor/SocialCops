package com.hanuorsocialcops.socialcops.CameraUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanuorsocialcops.socialcops.R;

/**
 * Created by Shantanu Johri on 9/24/2016.
 */

    class CustomView extends FrameLayout {

        ImageView textView;

        public CustomView(Context context) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.galleryitem, this);
            textView = (ImageView)getRootView().findViewById(R.id.thumbImage);
        }

        public void display(Bitmap bmp, boolean isSelected) {
            Log.d("Hiii",":0");
            textView.setImageBitmap(bmp);
            display(isSelected);
        }

        public void display(boolean isSelected) {
            textView.setBackgroundColor(isSelected? Color.LTGRAY : Color.WHITE);
        }
    }
