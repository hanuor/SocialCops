package com.hanuorsocialcops.socialcops.CameraUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanuorsocialcops.socialcops.R;

/**
 * Created by Shantanu Johri on 9/25/2016.
 */

public class UploadFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.uploadfrag,container,false);
        return v;
    }
}
