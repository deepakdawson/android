package com.example.jackdawson.fourplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;

public class VideoRocorder extends AppCompatActivity {

    private static final int CAMERA_PERMISION=0;
    ImageButton videoRecord;

    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_rocorder);
        checkCameraPermission();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Video Recorder");
        final Animation rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        videoRecord=(ImageButton)findViewById(R.id.video_record_button);
        videoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //
                // rotate the view
                v.startAnimation(rotate);
                // create new Intentwith with Standard Intent action that can be
                // sent to have the camera application capture an video and return it.
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                // start the Video Capture Intent
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        });

    }



    /** Create a file Uri for saving an image or video */
    private  Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaFile(type));
    }


    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(int type){

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/Four Player Video Recorder");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){
                Toast.makeText(getApplicationContext(), "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if(type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Four Player Video Recorder" + File.separator +
                    "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // After camera screen this code will excuted

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(getApplicationContext(),"video saved to sdcard ",Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();

            } else {
                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }





    private void checkCameraPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

                    == PackageManager.PERMISSION_GRANTED){
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(getApplicationContext(),"App needs permission for CAMERA",Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String [] {
                                Manifest.permission.CAMERA},
                      CAMERA_PERMISION);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permission, @NonNull int [] grantResults){

        switch (requestCode){

            case CAMERA_PERMISION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("camera permission ","granted");
                    Toast.makeText(getApplicationContext(),"Camera Permission granted",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permission,grantResults);

        }
    }

}
