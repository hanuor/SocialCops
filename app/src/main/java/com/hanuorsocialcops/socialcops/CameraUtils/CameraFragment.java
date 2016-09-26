package com.hanuorsocialcops.socialcops.CameraUtils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
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
     Camera camera = null;
    SurfaceView surfaceView;
    RelativeLayout afterClick;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    MediaRecorder mediaRecorder;
    boolean recording;
    LinearLayout mainLayout;
    RelativeLayout cameraRel,gify;
    private boolean checkWriteExternalPermission()
    { boolean counter = true;
        String permission1 = "android.permission.WRITE_EXTERNAL_STORAGE";
        String permission2 = "android.permission.CAMERA";

        String permission3 = "android.permission.READ_EXTERNAL_STORAGE";

        String permission5 = "android.permission.RECORD_AUDIO";
        int res = getContext().checkCallingOrSelfPermission(permission1);

        int res2 = getContext().checkCallingOrSelfPermission(permission2);
        int res3 = getContext().checkCallingOrSelfPermission(permission3);

        int res5 = getContext().checkCallingOrSelfPermission(permission5);
        if(res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED &&
                res3 == PackageManager.PERMISSION_GRANTED
                && res5 == PackageManager.PERMISSION_GRANTED){

        }else{
            counter = false;
        }
        return counter;
    }

    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.coffeeDark));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.frag1_fragemet,container,false);
        setTranslucentStatusBar(getActivity().getWindow());
        final FloatingActionButton captureImage = (FloatingActionButton) v.findViewById(R.id.captureImage);
        final FloatingActionButton captureVideo = (FloatingActionButton) v.findViewById(R.id.captureVideo);
        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        afterClick = (RelativeLayout) v.findViewById(R.id.afterClick);
        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceview);
        cameraRel = (RelativeLayout) v.findViewById(R.id.cameraRel);
        gify = (RelativeLayout)v.findViewById(R.id.gify);
        mainLayout = (LinearLayout) v.findViewById(R.id.mainLayout);
        ImageView img = (ImageView) v.findViewById(R.id.gifvideo);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(img);
        Glide.with(this).load(R.drawable.giphy).into(imageViewTarget);
        recording = false;
        mediaRecorder = new MediaRecorder();
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

        cameraRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.INVISIBLE);
                afterClick.setVisibility(View.VISIBLE);
                captureImage.setVisibility(View.VISIBLE);

                if(!previewing){

                    try {
                        releaseCameraAndPreview();
                       camera = Camera.open();
                    } catch (Exception e) {
                        Log.e(getString(R.string.app_name), "failed to open Camera");
                        e.printStackTrace();
                    }
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
            }
        });
        gify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.INVISIBLE);
                afterClick.setVisibility(View.VISIBLE);
                captureVideo.setVisibility(View.VISIBLE);
                /*camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                if (camera != null){
                        camera.setDisplayOrientation(90);
                        camera.startPreview();

                }*/
            }
        });
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWriteExternalPermission()){
                    if (recording) {

                        mediaRecorder.stop();
                        mediaRecorder.release();
                        Log.d("transition", "stop");

                        final Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                        fadeIn.setDuration(2000);
                        mainLayout.setAnimation(fadeIn);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainLayout.setVisibility(View.VISIBLE);
                                afterClick.setVisibility(View.INVISIBLE);

                            }
                        }, 1000);
                        //  buttonStartCameraPreview.setVisibility(View.VISIBLE);
                    } else {
                        mediaRecoderSettings();

                        mediaRecorder.start();
                        recording = true;
                        Log.d("transition", "start");

                    }
                }else{
                    Toast.makeText(getActivity(), "Please grant the necessary permissions", Toast.LENGTH_SHORT).show();
                }



            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWriteExternalPermission()){
                    camera.takePicture(shutterCallback,rawCallback,jpegCallback);
                }else{
                    Toast.makeText(getActivity(), "Please grant the necessary permissions", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return v;
    }

    private void releaseCameraAndPreview() {
      //  myCameraPreview.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }
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
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private void SaveImage(Bitmap finalBitmap) {
        Bitmap newBitmap = RotateBitmap(finalBitmap,90);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/socialCopsDemo/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        camera.release();
        mainLayout.setAnimation(fadeIn);
        mainLayout.setVisibility(View.VISIBLE);
        afterClick.setVisibility(View.INVISIBLE);
    }
    private void saveVideoThumbnail(Bitmap bmp, String root){
        File myDir = new File(root);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void mediaRecoderSettings(){
        Log.d("insomania","1");
        Random generator = new Random();
        int n = 10000;
        String root = Environment.getExternalStorageDirectory().toString()+"/socialCopsDemo/";
        n = generator.nextInt(n);
        String fname = "Vid-"+ n;
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        CamcorderProfile camcorderProfile_HQ = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mediaRecorder.setProfile(camcorderProfile_HQ);
         mediaRecorder.setOutputFile(root+fname+".mp4" );
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getActivity(), "IllegalStateException called", Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getActivity(), "prepare() failed", Toast.LENGTH_LONG).show();

        }


    }

    private void prepareMediaRecorder(){
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        Log.d("insom","9");
        try {
            Log.d("insomania","2");
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
