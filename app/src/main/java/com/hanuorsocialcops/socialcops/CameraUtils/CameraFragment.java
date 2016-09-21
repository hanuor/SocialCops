package com.hanuorsocialcops.socialcops.CameraUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hanuorsocialcops.socialcops.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.view.View.GONE;


/**
 * Created by Shantanu Johri on 9/20/2016.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    MediaRecorder mediaRecorder;
    boolean recording;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag1_fragemet,container,false);
        final FloatingActionButton buttonStartCameraPreview = (FloatingActionButton) v.findViewById(R.id.startcamerapreview);
        final FloatingActionButton videoRecorder = (FloatingActionButton) v.findViewById(R.id.videoRecord);
        final FloatingActionButton captureImage = (FloatingActionButton) v.findViewById(R.id.captureImage);
        final FloatingActionButton captureVideo = (FloatingActionButton) v.findViewById(R.id.captureVideo);
        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceview);
        recording = false;
        mediaRecorder = new MediaRecorder();
        mediaRecoderSettings();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    SaveImage(bitmap);
                } catch (Exception e) {

                }
            }
        };

        buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonStartCameraPreview.setVisibility(GONE);
                captureImage.setVisibility(View.VISIBLE);
                videoRecorder.setVisibility(GONE);
                if(!previewing){
                    camera = Camera.open();
                    if (camera != null){
                        try {
                            camera.setDisplayOrientation(90);
                            camera.setPreviewDisplay(surfaceHolder);
                            camera.startPreview();
                            previewing = true;
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }});
        videoRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartCameraPreview.setVisibility(GONE);
                captureVideo.setVisibility(View.VISIBLE);
                captureImage.setVisibility(GONE);
             videoRecorder.setVisibility(GONE);

            }
        });
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording){
                    videoRecorder.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.pause));
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    Log.d("trans","stop");


                }else{
                    videoRecorder.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.play));
                    mediaRecorder.start();
                    recording = true;
                    Log.d("trans","start");

                }
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(shutterCallback,rawCallback,jpegCallback);
            }
        });


        return v;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        prepareMediaRecorder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/socialCopsDemo");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void mediaRecoderSettings(){
        Random generator = new Random();
        int n = 10000;
        String root = Environment.getExternalStorageDirectory().toString()+"/socialCopsDemo/";
        n = generator.nextInt(n);
        String fname = "Vid-"+ n;
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        CamcorderProfile camcorderProfile_HQ = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mediaRecorder.setProfile(camcorderProfile_HQ);
        mediaRecorder.setOrientationHint(90);
        mediaRecorder.setOutputFile(root+fname+".mp4" );
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
    }

    private void prepareMediaRecorder(){
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
