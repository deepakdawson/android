package com.example.jackdawson.fourplayer;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.os.Message;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jack dawson on 4/16/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
, MediaPlayer.OnCompletionListener{


    private static final String CMD_PLAY="play";
    private static final String CMD_STOP="stop";
    private static final String CMD__PAUSE="pause";
    private static final String CMD_NEXT="next";
    private static final String CMD_PRE="previous";
    private static final int CMD_SEEK=0;
    private static final String ACTION="Music Action";
    private static final int play=0;
    private static final int pause=1;
    private static final int next=2;
    private static final int previous=3;
    private static final int stop=4;


    //private static  ArrayList<MediaFileInfo> listMedia=new ArrayList<MediaFileInfo>();

    private boolean isFilled=false;
    private static int position;
    private static int currentplaying;
   // private static int POINTER;
    private static int playbackState;
    private static int currentPosition;
    MusicHandlerThread musicHandlerThread;
    boolean stopped=false;
    boolean stopThread=true;
    boolean listClickplay=false;

    private  MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
   private  ArrayList<MediaFileInfo> listMedia;
    private static MediaFileInfo mediaFileInfo;

    private IBinder mBinder=new MusicBinder();

    private static ArrayList<MediaFileInfo> Queue=new ArrayList<MediaFileInfo>();

    /////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    private String title;
    private String artist;

    @Override
    public void onCreate(){
       // listMedia=Audio.mediaList;
        fillQueue();
        Log.d("size of Queue",String.valueOf(Queue.size()));
         //reference released
        if(isFilled){
        listMedia=null;
       }
        /**
         * start handler thread for background music
         */
        musicHandlerThread=new MusicHandlerThread("Music Service");
        musicHandlerThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        musicHandlerThread.start();
        //prepare intent filter for action
        intentFilter=new IntentFilter();
        addAction(intentFilter);
        // Register Local Broadcast Manager For Receiving Messages
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonBroadcastReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startid){

        //
        // get action
        String action=intent.getAction();
        if(action.equalsIgnoreCase(ACTION)){
            Bundle bundle=intent.getExtras();
            position=bundle.getInt("position");
           // Queue=bundle.getParcelableArrayList("media list");
            try {
                Log.d("size of list", String.valueOf(Queue.size()));
            }catch (NullPointerException e)
            {e.printStackTrace();}
            if(mediaPlayer!=null){
                stopPlayback();
                resetMediaPlayer();
                release();
            }
            play(position);
            listClickplay=true;
            Log.d("value of currnetplaying",String.valueOf(currentplaying));
        }
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

   @Override
   public void onRebind(Intent intent){

   }

    @Override
    public boolean onUnbind(Intent intent){

      return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /**
     *  funtion that manage list with class objects
     */

    private void fillQueue(){

         for(int i=0;i<listMedia.size();i++){
          Queue.add(listMedia.get(i));
         }
         if(Queue.size()==listMedia.size()){
            isFilled=true;
             Log.d("Queue filled","Successfully");
             Log.d("size=",String.valueOf(Queue.size()));
         }

     }


    public class MusicBinder extends Binder {

       MusicService getService(){
           return MusicService.this;
       }
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *
     * Callback routines
     *
     * /////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
     *
     */


    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }

    public boolean isListClickplayed(){
        return listClickplay;
    }

    public void suspendMediaPlayer(){
        stopPlayback();
        release();
    }

    public boolean getMediaPlayer(){
        if(mediaPlayer== null){
            return true;
        }else{
            return false;
        }
    }

    public boolean isPlayingMediaPlayer(){
        //
       //check if media playr is playing
       if(mediaPlayer.isPlaying()){
           return true;
      }
        return false;
    }

    public void setDataSource(String path){

        try{
            mediaPlayer.setDataSource(path);
        }
        catch (Exception e){
            e.getCause();
        }
    }

    public void prepare(){
        try {
            mediaPlayer.prepare();
        }catch (IOException e){
            e.getCause();
        }
    }

    public void stopPlayback(){
        try{
            mediaPlayer.stop();
        }catch (IllegalStateException e)
        {e.printStackTrace();}
    }

    //
    // reset media player
    public void resetMediaPlayer(){
        mediaPlayer.reset();
    }

    public int getDurationMediaPlayer(){
        return mediaPlayer.getDuration();
    }

    public int getCurrentPositionMediaPlayer(){
        try{
           if(isPlayingMediaPlayer()){
            return mediaPlayer.getCurrentPosition();
           }
        }catch (IllegalStateException e){
            e.printStackTrace();;
        }
        return 0;
    }

    public void release(){
        try {
            mediaPlayer.release();
        }catch (IllegalStateException e)
        {e.printStackTrace();}
    }

    public void start(){
        try {
            mediaPlayer.start();
        }catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }


    /**
     *   mdia player on completion listener
     *
     */

    @Override
    public void onCompletion(MediaPlayer mediaPlayer){
        playNext(getNext());
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer){
       start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer,int arg0,int arg1){
        Button button;
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.error_media_player_dialog);
        button=(Button) dialog.findViewById(R.id.ok);
      //  dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
       Log.d("Media player:","internal error occurred");
        return true;
    }

    //
    //  broadcast receiver


    private BroadcastReceiver buttonBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            switch (action){

                case CMD_PLAY:
                    Message message=musicHandlerThread.getHandler().obtainMessage(play);
                    musicHandlerThread.postMessage(message);

                    break;
                case CMD__PAUSE:
                    Message msg1=musicHandlerThread.getHandler().obtainMessage(pause);
                    musicHandlerThread.postMessage(msg1);
                    break;
                case CMD_NEXT:
                    Message msg2=musicHandlerThread.getHandler().obtainMessage(next);
                    musicHandlerThread.postMessage(msg2);
                    break;
                case CMD_PRE:
                    Message msg3=musicHandlerThread.getHandler().obtainMessage(previous);
                    musicHandlerThread.postMessage(msg3);
                    break;
                case CMD_STOP:
                    Message msg4=musicHandlerThread.getHandler().obtainMessage(stop);
                    musicHandlerThread.postMessage(msg4);
                    break;

            }

        }
    };

    public void addAction(IntentFilter intentFilter1){

        intentFilter1.addAction(CMD__PAUSE);
        intentFilter1.addAction(CMD_NEXT);
        intentFilter1.addAction(CMD_PLAY);
        intentFilter1.addAction(CMD_PRE);
        intentFilter1.addAction(CMD_STOP);

    }

    /**
     *  function play that play song in background
     */

    public void play(int position /**  i is here position in ArrayList*/){

        //
        // check if it is running
        if(mediaPlayer!=null  ){
            start();
        }else {
            /**
             * if it is not ruuning then is it is first tim to play music
             */
            mediaFileInfo = Queue.get(position);
            currentplaying = position;
            //
            // create new media player
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            //set data source
            setDataSource(mediaFileInfo.getFilePath());
            ///
            // prepare
            prepare();
            //start();
        }
    }

    public void seekToPosition(int progress){
        mediaPlayer.seekTo(progress);
    }


    public void pause(){
        if(isPlayingMediaPlayer()){
             mediaPlayer.pause();
        }
    }
      public int getNext(){
          currentplaying=(currentplaying+1)% Queue.size();
          return currentplaying;
      }


    public int getPrevious(){
        currentplaying=(currentplaying-1<0)? Queue.size()-1:currentplaying-1;
        return currentplaying;
    }
    public void playNext(int position1){
        if(isPlayingMediaPlayer()){
            pause();
            stopPlayback();
           resetMediaPlayer();
        }else{
            stopPlayback();
            resetMediaPlayer();
        }
        mediaFileInfo=Queue.get(position1);
        artist=mediaFileInfo.getArtist();
        title=mediaFileInfo.getFileName();
        setDataSource(mediaFileInfo.getFilePath());
        prepare();

    }
    public void playprevious(int position){
        if(isPlayingMediaPlayer()){
            pause();
            stopPlayback();
            resetMediaPlayer();
        }
        else
        {
            stopPlayback();
            resetMediaPlayer();
        }

        mediaFileInfo=Queue.get(position);
        artist=mediaFileInfo.getArtist();
        title=mediaFileInfo.getFileName();
        setDataSource(mediaFileInfo.getFilePath());
        prepare();
    }

    public boolean destroyService(){
      Intent intent=new Intent();
        intent.setClass(this,MusicService.class);
        intent.setAction("Music Action");
        intent.setPackage("com.example.jackdawson.fourplayer");
        musicHandlerThread.quitMusichandler();
        stopped=stopService(intent);
        return stopped;
    }

    /**
     * thread class
     */

      public class MusicHandlerThread extends HandlerThread{

        private Handler handler;
        //
        //  public constructor
        public MusicHandlerThread( String name){
            super(name);
        }

        @Override
        protected void onLooperPrepared(){
            handler=new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg){
                    //
                    //  check what message is
                    switch (msg.what){

                        case 0:
                            play(currentplaying);
                            Log.d("handler thread=="+MusicHandlerThread.currentThread().toString(),"plsy");
                            break;
                        case 1:
                            pause();
                            Log.d("handler thread=="+MusicHandlerThread.currentThread().toString(),"pause");
                            break;
                        case 2:
                            playNext(getNext());
                            Log.d("handler thread=="+MusicHandlerThread.currentThread().toString(),"next");
                            break;
                        case 3:
                            playprevious(getPrevious());
                            Log.d("handler thread=="+MusicHandlerThread.currentThread().toString(),"previous");
                            break;
                        case 4:
                            suspendMediaPlayer();
                            Log.d("handler thread=="+MusicHandlerThread.currentThread().toString(),"stop");
                            break;
                    }


                }
            };
        }

        public void quitMusichandler(){
            this.quitSafely();
        }

        public void postMessage(Message msg){
            handler.sendMessage(msg);
        }

        public Handler getHandler(){
            return this.handler;
        }

    }


}
