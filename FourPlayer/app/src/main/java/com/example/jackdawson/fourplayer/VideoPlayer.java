package com.example.jackdawson.fourplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;


public class VideoPlayer extends AppCompatActivity implements SurfaceHolder.Callback ,MediaPlayer.OnPreparedListener{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaController mediaController;
    AudioManager audioManager;
    MediaPlayer mediaPlayer=null;
    private static String path;
     GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        path=bundle.getString("path");
        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar=getSupportActionBar();
        try {
            actionBar.hide();
        }catch (NullPointerException e){
            e.getCause();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        surfaceView=(SurfaceView) findViewById(R.id.surfaceView);
          gestureDetectorCompat=new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
              @Override
              public boolean onDown(MotionEvent e) {
                  if(mediaController.isShowing()){
                      mediaController.hide();
                  }else {
                      mediaController.show();
                  }
                  return true;
              }

              @Override
              public void onShowPress(MotionEvent e) {

              }

              @Override
              public boolean onSingleTapUp(MotionEvent e) {

                  return false;
              }

              @Override
              public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                  return false;
              }

              @Override
              public void onLongPress(MotionEvent e) {

              }

              @Override
              public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                  return false;
              }
          });
        mediaController=new MediaController(this);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        }
        catch (Exception e){
            e.getMessage();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer1){
        mediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
            @Override
            public void start() {
                mediaPlayer.start();
            }

            @Override
            public void pause() {
                mediaPlayer.pause();
            }

            @Override
            public int getDuration() {
                return mediaPlayer.getDuration();
            }

            @Override
            public int getCurrentPosition() {
                return mediaPlayer.getCurrentPosition();
            }

            @Override
            public void seekTo(int pos) {
                mediaPlayer.seekTo(pos);
            }

            @Override
            public boolean isPlaying() {
                return mediaPlayer.isPlaying();
            }

            @Override
            public int getBufferPercentage() {
                return 0;
            }

            @Override
            public boolean canPause() {
                return true;
            }

            @Override
            public boolean canSeekBackward() {
                return true;
            }

            @Override
            public boolean canSeekForward() {
                return true;
            }

            @Override
            public int getAudioSessionId() {
                return mediaPlayer.getAudioSessionId();
            }
        });
        mediaController.setAnchorView(surfaceView);
        mediaController.setEnabled(true);
        mediaPlayer.start();
        mediaController.show();
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
         gestureDetectorCompat.onTouchEvent(ev);
      return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format ,int w,int h){
        mediaPlayer.setDisplay(holder);

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {

            mediaPlayer.setDisplay(holder);
            this.notifyAll();

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            synchronized(this)          {
                this.notifyAll();
            }
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
    }
}
