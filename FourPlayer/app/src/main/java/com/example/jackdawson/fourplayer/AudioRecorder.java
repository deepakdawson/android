package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

public class AudioRecorder extends AppCompatActivity implements View.OnClickListener{

    private ImageButton start,stop,viewRecord;
    private Chronometer chronometer;
    //private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder=null;
    private String outputFile;
    File file;
    boolean isCreated=false;
    Random random;
    private static int AUDIO_PERMISSION=1;

     Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_recorder);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Sound Recorder");
        requestPermission();
        start=(ImageButton)findViewById(R.id.record_audio);
        viewRecord=(ImageButton)findViewById(R.id.record_view);
        stop=(ImageButton)findViewById(R.id.record_audio_stop);
        start.setOnClickListener(this);
        viewRecord.setOnClickListener(this);
        stop.setOnClickListener(this);
        chronometer=(Chronometer)findViewById(R.id.record_audio_time);
        random=new Random();
         rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);
        mediaRecorder=new MediaRecorder();

        file=new File(Environment.getExternalStorageDirectory(),"/Four Player Audio Recorder");

    }



    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.record_audio:
                start.startAnimation(rotate);
                if(file.exists()){
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    outputFile=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Four Player Audio Recorder/record"+random.nextInt(100)+".3gpp";
                    mediaRecorder.setOutputFile(outputFile);
                }else{
                    isCreated=file.mkdir();
                    if(isCreated){
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        outputFile=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Four Player Audio Recorder/record"+random.nextInt(100)+".3gpp";
                        mediaRecorder.setOutputFile(outputFile);
                        Log.d("file directory created","successfully");
                    }
                    Log.d("file directory created","not");
                    Toast.makeText(getApplicationContext(),"Storage Permission not available",Toast.LENGTH_LONG).show();
                }

                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (Exception e){
                    e.getMessage();
                }
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
               // Log.d("state",mediaRecorder.toString());
                break;
            case R.id.record_audio_stop:
                    stop.startAnimation(rotate);
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    mediaRecorder.stop();
                    stop.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.VISIBLE);

                break;
            case R.id.record_view:
                viewRecord.startAnimation(rotate);
                Intent intent =new Intent();
                intent.setClass(getApplicationContext(),AudioRecordView.class);
                startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        //
        // check whether media recorder i null or not
        if(mediaRecorder == null){
            super.onBackPressed();
        }else {
            mediaRecorder.release();
            mediaRecorder = null;
            super.onBackPressed();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{ RECORD_AUDIO,WRITE_EXTERNAL_STORAGE}, AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode==AUDIO_PERMISSION){

            if(grantResults.length>0){

                boolean audio=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                boolean storage=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(audio && storage){
                   // Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Permission not granted",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public boolean checkPermission() {
        int result= ContextCompat.checkSelfPermission(getApplicationContext(),RECORD_AUDIO);
        return result==PackageManager.PERMISSION_DENIED;
    }

}
