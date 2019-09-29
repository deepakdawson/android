package com.example.jackdawson.fourplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MusicPlayer extends AppCompatActivity implements View.OnClickListener ,SeekBar.OnSeekBarChangeListener{

    /**
     * commands for playback state
     *
     */

    private static final String CMD_PLAY="play";
    private static final String CMD_STOP="stop";
    private static final String CMD__PAUSE="pause";
    private static final String CMD_NEXT="next";
    private static final String CMD_PRE="previous";
    private static final int CMD_SEEK=0;
    private static final String ACTION="Music Action";

    //collecet data from fragments
   private  ArrayList<MediaFileInfo> mediaList;

    // position of song in list
    private int position;
    LocalBroadcastManager localBroadcastManager;

    MusicService musicService;
   private static Thread upateSeekbar;
   private Handler uiHandler;
    private boolean isConnected=false;
    public boolean stopThread=true;
    boolean uipost=true;

   // int totalDuration;
    //
    // make rference for each views in layout

    private ImageButton play,next, previous;
    public SeekBar seekBar;
    TextView title, artist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        uiHandler=new Handler(getMainLooper());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //
        // collect reference
        play=(ImageButton)findViewById(R.id.playButton);
        next=(ImageButton)findViewById(R.id.nextButton);
        previous=(ImageButton)findViewById(R.id.previousButton);
        title=(TextView)findViewById(R.id.music_activity_title);
        artist=(TextView)findViewById(R.id.music_activity_artist);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        /**
         * get data from intent
         */
        Intent data=getIntent();
        Bundle bundle=data.getExtras();
        position=bundle.getInt("position");
       // mediaList=bundle.getParcelableArrayList("media list");
        /**
         *  make ready for new Intent
         */
        Intent intent=new Intent();
        intent.putExtra("position",position);
        intent.putExtra("media list",  mediaList);
        intent.setAction(ACTION);
        intent.setPackage("com.example.jackdawson.fourplayer");
        intent.setClass(this,MusicService.class);
        startService(intent);
        artist.setText(mediaList.get(position).getArtist());
        title.setText(mediaList.get(position).getFileName());


    }


    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder=(MusicService.MusicBinder)service;
            musicService=musicBinder.getService();
            isConnected=true;
            Log.d("value of is connectec",String.valueOf(isConnected));
            upateSeekbar.start();
            if(musicService.isListClickplayed()){

            }
            // Log.d("value if is playing",String.valueOf(musicService.isPlayingMediaPlayer()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected=false;
        }
    };


    @Override
    public void onResume(){
        super.onResume();
        upateSeekbar=new Thread(new Runnable() {
            @Override
            public void run() {
                int cuurentPosition=0;
                int total=musicService.getDurationMediaPlayer();
                seekBar.setMax(total);
                while(cuurentPosition< total && stopThread) {
                    total=musicService.getDurationMediaPlayer();
                    try {
                        Thread.sleep(500);
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                               if(uipost) {
                                   seekBar.setProgress(musicService.getCurrentPositionMediaPlayer());
                               }
                            }
                        });

                    }catch (InterruptedException e){
                        e.getMessage();
                    }
                }
            }
        });

        Log.d("seekbar thread status",Thread.currentThread().toString());
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            musicService.seekToPosition(seekBar.getProgress());

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
             musicService.seekToPosition(seekBar.getProgress());
    }



   //   Bundle bundle=intent.getExtras();
   // private int position=bundle.getInt("position");
  // // private  ArrayList<MediaFileInfo> mediaList=(ArrayList) bundle.getParcelableArrayList("media list");



    //
    //  manage each button call

    @Override
    public void onClick(View view){

        int id =view.getId();
        switch (id){

            case R.id.playButton:

                if(isConnected) {
                    if (musicService.isPlayingMediaPlayer()) {
                      //  musicService.pause();
                        sendMessage(CMD__PAUSE);
                        play.setImageResource(R.drawable.ic_pause_white_36dp);
                    } else {
                       // musicService.playNext();
                        sendMessage(CMD_PLAY);
                        play.setImageResource(R.drawable.ic_play_white_36dp);
                    }
                }
                else{

                    Log.d("service ","disconnected");
                }
                break;

            case R.id.nextButton:
                if(isConnected) {
                    //musicService.playNext();
                    if(musicService.isPlayingMediaPlayer() == false){
                        play.setImageResource(R.drawable.ic_play_white_36dp);
                    }
                    Log.d("value of current ppal",String.valueOf(musicService.getNext()));
                    uipost=false;
                    sendMessage(CMD_NEXT);
                    uipost=true;
                      seekBar.setMax(musicService.getDurationMediaPlayer());
                    title.setText(musicService.getTitle());
                    artist.setText(musicService.getArtist());

                                   }
                else{
                   // musicService.playprevious();
                    Log.d("service ","disconnected");
                }
                break;

            case R.id.previousButton:
                if(isConnected) {
                    if(musicService.isPlayingMediaPlayer() == false){
                        play.setImageResource(R.drawable.ic_play_white_36dp);
                    }
                    uipost=false;
                   sendMessage(CMD_PRE);
                    uipost=true;
                   seekBar.setMax(musicService.getDurationMediaPlayer());
                    artist.setText(musicService.getArtist());
                    title.setText(musicService.getTitle());
                }
                else{
                    Log.d("service ","disconnected");
                }
                break;

        }

    }

    @Override
    public void onBackPressed(){
        //
        // check if music is paused
        try {
            if (musicService.isPlayingMediaPlayer() == false) {
                sendMessage(CMD_STOP);

                if(musicService.getMediaPlayer()) {
                    musicService.destroyService();

                }
            }
        }catch (Exception e){
            e.getCause();
        }
        super.onBackPressed();

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(serviceConnection);
    }
    @Override
    public void onStart(){
        super.onStart();
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        Intent intent=new Intent();
        intent.setClass(this,MusicService.class);
        intent.setAction("Music Action");
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
        public void sendMessage(String message){
            Intent intent=new Intent(message);
            localBroadcastManager.sendBroadcast(intent);
        }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
   /* public  class SeekbarHandler extends HandlerThread{

        public Handler handler;

        public SeekbarHandler(String name){
            super(name);
        }


        @Override
        protected void onLooperPrepared(){
            //handler=new Handler(getLooper());


        }
        public void quitSeekbarhandler(){
            this.quitSafely();
        }

        public void postSeekbarMessage(Message msg){
            this.handler.sendMessage(msg);
        }

        public Handler getSeekbarHandler(){
            return this.handler;
        }

    }
    */

          /* public class SeekHandler extends Handler{
               public SeekHandler(Looper looper){
                   super(looper);
               }
               @Override
               public void handleMessage(Message msg){
                   int startTime=0;
                   int duration=msg.what/1000;
                   Log.d("duration ",String.valueOf(duration));
                   seekBar.setMax(duration);
                   seekBar.setProgress(startTime);
                   while(startTime < duration){
                       try {
                           Thread.sleep(1000);
                           uiHandler.post(new Runnable() {
                               @Override
                               public void run() {
                                   seekBar.setProgress(musicService.getCurrentPositionMediaPlayer()/1000);
                               }
                           });
                       }catch (InterruptedException e){
                           e.printStackTrace();
                       }catch (Exception e){
                           e.getCause();
                       }

                   }

               }
           }
*/
}
